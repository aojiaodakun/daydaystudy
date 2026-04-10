package com.hzk.pigeon;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class HashQueue1<K, V> {

    private static class Shard<V> {
        final ReentrantLock lock = new ReentrantLock();
        final Condition notEmpty = lock.newCondition();
        final Condition notFull = lock.newCondition();

        final List<LinkedList<V>> queues = new ArrayList<>();
        final Map<String, Integer> queueIndexMap = new HashMap<>();
        int takeQueueIndex = 0;
    }

    private final int SHARD_SIZE = 2;
    private final Shard<V>[] shards;
    private final int capacity;
    private final AtomicInteger count = new AtomicInteger();
    private final ReentrantLock globalLock = new ReentrantLock();
    private final Condition notEmptyGlobal = globalLock.newCondition();
    private volatile int takeShardIndex = 0; // 全局轮询 shard 索引

    @SuppressWarnings("unchecked")
    public HashQueue1(int capacity) {
        if (capacity <= 0) throw new IllegalArgumentException();
        this.capacity = capacity;
        shards = new Shard[SHARD_SIZE];
        for (int i = 0; i < SHARD_SIZE; i++) shards[i] = new Shard<>();
    }

    private int shardIndex(K key) {
        return Math.abs(key.hashCode()) % SHARD_SIZE;
    }

    private LinkedList<V> getOrCreateQueue(Shard<V> shard, String queueName) {
        Integer idx = shard.queueIndexMap.get(queueName);
        if (idx != null) return shard.queues.get(idx);
        LinkedList<V> newQueue = new LinkedList<>();
        shard.queues.add(newQueue);
        shard.queueIndexMap.put(queueName, shard.queues.size() - 1);
        return newQueue;
    }

    public void put(K key, V value) throws InterruptedException {
        Shard<V> shard = shards[shardIndex(key)];
        shard.lock.lock();
        try {
            while (count.get() >= capacity) shard.notFull.await();
            LinkedList<V> queue = getOrCreateQueue(shard, key.toString());
            queue.add(value);
            count.incrementAndGet();
            shard.notEmpty.signal();
        } finally {
            shard.lock.unlock();
        }
        globalLock.lock();
        try {
            notEmptyGlobal.signal();
        } finally {
            globalLock.unlock();
        }
    }

    public void put(Map<K, List<V>> map) throws InterruptedException {
        for (Map.Entry<K, List<V>> e : map.entrySet()) {
            Shard<V> shard = shards[shardIndex(e.getKey())];
            shard.lock.lock();
            try {
                while (count.get() + e.getValue().size() > capacity) shard.notFull.await();
                LinkedList<V> queue = getOrCreateQueue(shard, e.getKey().toString());
                queue.addAll(e.getValue());
                count.addAndGet(e.getValue().size());
                shard.notEmpty.signal();
            } finally {
                shard.lock.unlock();
            }
        }
        globalLock.lock();
        try {
            notEmptyGlobal.signal();
        } finally {
            globalLock.unlock();
        }
    }

    public V take() throws InterruptedException {
        V result = null;
        while (result == null) {
            if (count.get() == 0) {
                globalLock.lock();
                try {
                    while (count.get() == 0) notEmptyGlobal.await();
                } finally {
                    globalLock.unlock();
                }
            }

            for (int i = 0; i < SHARD_SIZE; i++) {
                int idx = (takeShardIndex + i) % SHARD_SIZE;
                Shard<V> shard = shards[idx];
                shard.lock.lock();
                try {
                    int qCount = shard.queues.size();
                    if (qCount == 0) continue;

                    Integer emptyQueueIndex = null;
                    for (int j = 0; j < qCount; j++) {
                        int qIdx = (shard.takeQueueIndex + j) % qCount;
                        LinkedList<V> queue = shard.queues.get(qIdx);
                        if (!queue.isEmpty()) {
                            result = queue.removeFirst();
                            count.decrementAndGet();
                            shard.notFull.signal();
                            if (queue.isEmpty()) {
                                emptyQueueIndex = qIdx;
                            }
                            shard.takeQueueIndex = (qIdx + 1) % qCount;

                            if (qIdx == qCount - 1) {
                                // 下一个分片
                                takeShardIndex = (idx + 1) % SHARD_SIZE;
                            } else {
                                // 停留当前分片
                                takeShardIndex = idx % SHARD_SIZE;
                            }
                            break;
                        }
                    }

                    // 安全移除空队列
                    if (emptyQueueIndex != null) {
                        Integer finalEmptyQueueIndex = emptyQueueIndex;
                        shard.queueIndexMap.values().removeIf(index -> index == finalEmptyQueueIndex);
                        shard.queues.remove(emptyQueueIndex);
                        if (shard.takeQueueIndex > emptyQueueIndex) {
                            shard.takeQueueIndex--;
                        }
                    }
                    if (result != null) {
                        break;
                    }
                } finally {
                    shard.lock.unlock();
                }
            }
        }
        return result;
    }

    public void addFirst(K key, V value) {
        Shard<V> shard = shards[shardIndex(key)];
        shard.lock.lock();
        try {
            LinkedList<V> queue = getOrCreateQueue(shard, key.toString());
            queue.addFirst(value);
            count.incrementAndGet();
            shard.notEmpty.signal();
        } finally {
            shard.lock.unlock();
        }
        globalLock.lock();
        try {
            notEmptyGlobal.signal();
        } finally {
            globalLock.unlock();
        }
    }

    public List<V> peek(int rounds) {
        if (rounds <= 0) throw new IllegalArgumentException();
        List<V> resultList = new LinkedList<>();
        int maxSize = SHARD_SIZE * rounds - 1;
        int peekShard = takeShardIndex;
        for (int i = 0; i < maxSize; i++) {
            int idx = (peekShard + i) % SHARD_SIZE;
            Shard<V> shard = shards[idx];
            shard.lock.lock();
            try {
                for (LinkedList<V> queue : shard.queues) {
                    if (!queue.isEmpty()) resultList.add(queue.getFirst());
                }
            } finally {
                shard.lock.unlock();
            }
        }
        return resultList;
    }

    public int size() {
        return count.get();
    }
}
