package com.hzk.pigeon;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Predicate;

public class HashQueue<K, V> {

    private static class Shard<V> {
        final LinkedList<V> list = new LinkedList<>();
        final ReentrantLock lock = new ReentrantLock();
        final Condition notEmpty = lock.newCondition();
        final Condition notFull = lock.newCondition();
    }

    private final int SHARD_SIZE = 8;// 分片
    private final Shard<V>[] shards;
    private final int capacity;// 容量
    private final AtomicInteger count = new AtomicInteger();
    private final ReentrantLock globalLock = new ReentrantLock();
    private final Condition notEmptyGlobal = globalLock.newCondition();
    private volatile int takeShard = 0;

    @SuppressWarnings("unchecked")
    public HashQueue(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException();
        }
        this.capacity = capacity;
        shards = new Shard[SHARD_SIZE];
        for (int i = 0; i < SHARD_SIZE; i++) {
            shards[i] = new Shard<>();
        }
    }

    private int shardIndex(K key) {
        return Math.abs(key.hashCode()) % SHARD_SIZE;
    }

    /** 单条放入 */
    public void put(K key, V value) throws InterruptedException {
        Shard<V> shard = shards[shardIndex(key)];
        shard.lock.lock();
        try {
            while (count.get() >= capacity) {
                shard.notFull.await();
            }
            shard.list.add(value);
            count.incrementAndGet();
            shard.notEmpty.signal();
        } finally {
            shard.lock.unlock();
        }
        // 唤醒全局等待的消费者
        globalLock.lock();
        try {
            notEmptyGlobal.signal();
        } finally {
            globalLock.unlock();
        }
    }

    /** 批量放入 */
    public void put(Map<K, List<V>> map) throws InterruptedException {
        for (Map.Entry<K, List<V>> entry : map.entrySet()) {
            K key = entry.getKey();
            Shard<V> shard = shards[shardIndex(key)];
            shard.lock.lock();
            try {
                while (count.get() >= capacity) {
                    shard.notFull.await();
                }
                for (V tempV : entry.getValue()) {
                    shard.list.add(tempV);
                    count.incrementAndGet();
                }
                shard.notEmpty.signal();
            } finally {
                shard.lock.unlock();
            }
        }
        // 唤醒全局等待的消费者
        globalLock.lock();
        try {
            notEmptyGlobal.signal();
        } finally {
            globalLock.unlock();
        }
    }


    /** 取出元素（全局阻塞等待） */
    public V take() throws InterruptedException {
        V result = null;
        while (result == null) {
            // 全局阻塞等待
            if (count.get() == 0) {
                globalLock.lock();
                try {
                    while (count.get() == 0) {
                        notEmptyGlobal.await();
                    }
                } finally {
                    globalLock.unlock();
                }
            }
            // 轮询各shard
            for (int i = 0; i < SHARD_SIZE; i++) {
                int idx = (takeShard + i) % SHARD_SIZE;
                Shard<V> shard = shards[idx];
                shard.lock.lock();
                try {
                    if (!shard.list.isEmpty()) {
                        result = shard.list.removeFirst();
                        count.decrementAndGet();
                        shard.notFull.signal();
                        takeShard = (idx + 1) % SHARD_SIZE;
                        break;
                    }
                } finally {
                    shard.lock.unlock();
                }
            }
        }
        return result;
    }

    /**
     * 窥视全部分片的队首
     * @param rounds 轮次
     * @return 全部分片的队首
     */
    public List<V> peek(int rounds) {
        if (rounds <= 0) {
            throw new IllegalArgumentException();
        }
        List<V> resultList = new LinkedList<>();
        int maxSize = SHARD_SIZE * rounds -1;
        int peekShard = takeShard;
        // 轮询各shard
        for (int i = 0; i < maxSize; i++) {
            int idx = (peekShard + i) % SHARD_SIZE;
            Shard<V> shard = shards[idx];
            shard.lock.lock();
            try {
                if (!shard.list.isEmpty()) {
                    V result = shard.list.getFirst();
                    resultList.add(result);
                }
            } finally {
                shard.lock.unlock();
            }
        }
        return resultList;
    }

    public void addFirst(K key, V value) {
        Shard<V> shard = shards[shardIndex(key)];
        shard.lock.lock();
        try {
            shard.list.addFirst(value);
            count.incrementAndGet();
            shard.notEmpty.signal();
        } finally {
            shard.lock.unlock();
        }
//        // 唤醒全局等待的消费者
//        globalLock.lock();
//        try {
//            notEmptyGlobal.signal();
//        } finally {
//            globalLock.unlock();
//        }
    }

    public List<V> getCopyShardValues(K key) {
        Shard<V> shard = shards[shardIndex(key)];
        shard.lock.lock();
        try {
            return new LinkedList<>(shard.list);
        } finally {
            shard.lock.unlock();
        }
    }

    /** moveMatchToEnd */
    public void moveMatchToEnd(K key, Predicate<V> predicate) {
        Shard<V> shard = shards[shardIndex(key)];
        shard.lock.lock();
        try {
            LinkedList<V> list = shard.list;
            Iterator<V> it = list.iterator();
            List<V> matched = new ArrayList<>();
            while (it.hasNext()) {
                V v = it.next();
                if (predicate.test(v)) {
                    matched.add(v);
                    it.remove();
                }
            }
            list.addAll(matched);
        } finally {
            shard.lock.unlock();
        }
    }

    public int getShardSize() {
        return SHARD_SIZE;
    }

    /** 当前总数量 */
    public int size() {
        return count.get();
    }
}

