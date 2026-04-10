package com.hzk.pigeon.task;


import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ListQueue<K, V> {

    static class QueueWrapper<V> {
        final LinkedList<V> list = new LinkedList<>();
        final ReentrantLock lock = new ReentrantLock();
    }
    // 队列集合
    private final List<QueueWrapper<V>> queueList = new ArrayList<>();
    // Map<队列名称, 队列索引>
    private final Map<K, Integer> queueIndexMap = new HashMap<>();
    // 活跃队列位图
    private final BitSet activeBitSet = new BitSet();
    // 全局计数器
    private final AtomicInteger count = new AtomicInteger(0);
    // 全局锁
    private final ReentrantLock globalLock = new ReentrantLock();
    private final Condition globalNotEmpty = globalLock.newCondition();
    // take下标
    private int takeIndex = 0;

    /** put 方法，支持高并发，优化锁粒度 */
    public void put(K key, V value) throws InterruptedException {
        Integer idx = queueIndexMap.get(key);
        QueueWrapper<V> queue;
        if (idx == null) {
            // 仅首次注册新队列时加全局锁
            globalLock.lock();
            try {
                idx = queueIndexMap.get(key);
                if (idx == null) {
                    idx = queueList.size();
                    queue = new QueueWrapper<>();
                    queueList.add(queue);
                    queueIndexMap.put(key, idx);
                } else {
                    queue = queueList.get(idx);
                }
            } finally {
                globalLock.unlock();
            }
        } else {
            queue = queueList.get(idx);
        }
        // 入队加单队列锁
        queue.lock.lock();
        try {
            queue.list.add(value);
            count.incrementAndGet();
            // 入队后标记为活跃队列
            synchronized (activeBitSet) {
                activeBitSet.set(idx);
            }
        } finally {
            queue.lock.unlock();
        }
        // 通知全局有消息
        globalLock.lock();
        try {
            globalNotEmpty.signalAll();
        } finally {
            globalLock.unlock();
        }
    }

    /** put 方法，支持高并发，优化锁粒度 */
    public void put(Map<K, List<V>> map) {
        for (Map.Entry<K, List<V>> entry : map.entrySet()) {
            K key = entry.getKey();
            List<V> values = entry.getValue();
            if (values == null || values.isEmpty()) {
                continue;
            }
            Integer idx = queueIndexMap.get(key);
            QueueWrapper<V> queue;
            if (idx == null) {
                synchronized (this) {
                    idx = queueIndexMap.get(key);
                    if (idx == null) {
                        idx = queueList.size();
                        queue = new QueueWrapper<>();
                        queueList.add(queue);
                        queueIndexMap.put(key, idx);
                    } else {
                        queue = queueList.get(idx);
                    }
                }
            } else {
                queue = queueList.get(idx);
            }

            queue.lock.lock();
            try {
                for (V v : values) {
                    queue.list.add(v);
                    count.incrementAndGet();
                }
                // 设置活跃位
                synchronized (activeBitSet) {
                    activeBitSet.set(idx);
                }
            } finally {
                queue.lock.unlock();
            }
        }
        // 通知全局有消息
        globalLock.lock();
        try {
            globalNotEmpty.signalAll();
        } finally {
            globalLock.unlock();
        }
    }

    /** take 方法，轮询所有活跃队列 */
    public V take() throws InterruptedException {
        V result = null;
        while (result == null) {
            if (count.get() == 0) {
                globalLock.lock();
                try {
                    while (count.get() == 0) {
                        globalNotEmpty.await();
                    }
                } finally {
                    globalLock.unlock();
                }
            }

            List<Integer> activeIndices = snapshotActiveIndices();
            int total = activeIndices.size();
            if (total == 0) {
                continue; // 稍后再试
            }
            for (int i = 0; i < total; i++) {
                int idx = activeIndices.get((takeIndex + i) % total);
                QueueWrapper<V> queue = queueList.get(idx);
                queue.lock.lock();
                try {
                    if (!queue.list.isEmpty()) {
                        result = queue.list.removeFirst();
                        count.decrementAndGet();
                        if (queue.list.isEmpty()) {
                            synchronized (activeBitSet) {
                                activeBitSet.clear(idx);
                            }
                        }
                        takeIndex = (idx + 1) % total;
                        break;
                    }
                } finally {
                    queue.lock.unlock();
                }
            }
        }
        return result;
    }

    /** peek 方法，可配置 rounds 轮询次数 */
    public List<V> peekRounds(int rounds) {
        List<V> result = new ArrayList<>();
        List<Integer> activeIndices = snapshotActiveIndices();
        int total = activeIndices.size();
        int maxSize = total * rounds - 1;
        int peekIndex = takeIndex;
        for (int i = 0; i < maxSize; i++) {
            int idx = activeIndices.get((peekIndex + i) % total);
            QueueWrapper<V> queue = queueList.get(idx);
            queue.lock.lock();
            try {
                if (!queue.list.isEmpty()) {
                    result.add(queue.list.peekFirst());
                }
            } finally {
                queue.lock.unlock();
            }
        }
        return result;
    }

    /** addFirst 方法：把元素加到指定队列头部 */
    public void addFirst(K key, V value) {
        Integer idx = queueIndexMap.get(key);
        QueueWrapper<V> queue = queueList.get(idx);
        queue.lock.lock();
        try {
            queue.list.addFirst(value);
            count.incrementAndGet();
            synchronized (activeBitSet) {
                activeBitSet.set(idx);
            }
        } finally {
            queue.lock.unlock();
        }
        globalLock.lock();
        try {
            globalNotEmpty.signalAll();
        } finally {
            globalLock.unlock();
        }
    }

    /** 生成活跃队列索引快照 */
    private List<Integer> snapshotActiveIndices() {
        List<Integer> list = new ArrayList<>();
        synchronized (activeBitSet) {
            for (int i = activeBitSet.nextSetBit(0); i >= 0; i = activeBitSet.nextSetBit(i + 1)) {
                list.add(i);
            }
        }
        return list;
    }

    /** 返回全局队列大小 */
    public int size() {
        return count.get();
    }

    public int size(K key) {
        Integer idx = queueIndexMap.get(key);
        QueueWrapper<V> queueWrapper = queueList.get(idx);
        return queueWrapper.list.size();
    }

}
