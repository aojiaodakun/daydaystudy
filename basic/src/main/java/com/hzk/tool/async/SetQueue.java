package com.hzk.tool.async;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/***
 * put Not duplicated task into the queue ,get the task to execute
 *
 * @author rd_edmund_he
 *
 * @param <E>
 */
public class SetQueue<E> {

    private ReentrantLock lock = new ReentrantLock(false);
    private Condition notEmpty = lock.newCondition();
    private Map<E, AtomicBoolean> m = new ConcurrentHashMap<>(128);// e, weather has
    // add list
    // for this
    // span
    private ConcurrentLinkedQueue<E> preRun = new ConcurrentLinkedQueue<>();

    public void putIfAbsent(E sr) {
        if (sr == null) {
            return;
        }
        boolean isEmpty = preRun.isEmpty();
        if (!m.containsKey(sr)) {
            synchronized (m) {
                if (m.size() > 1000) {
                    m.clear();
                }
                preRun.add(sr);
                if (!m.containsKey(sr)) {
                    m.put(sr, new AtomicBoolean(true));
                }
            }
        } else {
            AtomicBoolean ab = m.get(sr);
            if (ab != null && !ab.getAndSet(true)) {
                preRun.add(sr);
            }
        }
        if (isEmpty) {
            try {
                lock.lock();
                notEmpty.signal();
            } finally {
                lock.unlock();
            }
        }
    }

    public synchronized E poll() {
        try {
            lock.lock();
            E span = preRun.poll();
            if (span == null) {
                try {
                    notEmpty.await(100, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                }
                return null;
            } else {
                AtomicBoolean notDealTag = m.get(span);
                if (notDealTag != null) {
                    notDealTag.set(false);
                }

                return span;
            }

        } finally {
            lock.unlock();
        }
    }

}
