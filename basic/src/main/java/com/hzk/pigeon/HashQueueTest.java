package com.hzk.pigeon;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class HashQueueTest {

    static class Task {
        final String queueName;
        final String msg;
        Task(String queueName, String msg) {
            this.queueName = queueName;
            this.msg = msg;
        }
        @Override
        public String toString() {
            return "Task{" + queueName + "," + msg + '}';
        }
    }

    public static void main(String[] args) throws Exception {
        Map<Integer, List<String>> map = new HashMap<>();
        int total = 20;
        for (int i = 1; i <= total; i++) {
            String s = "";
            if (i < 10) {
                s = "coreLibTest.coreLibTest_1_00" + i;
            } else {
                s = "coreLibTest.coreLibTest_1_0" + i;
            }
            int sd = Math.abs(s.hashCode()) % 20;
            List<String> stringList = map.computeIfAbsent(sd, key -> new ArrayList<>());
            stringList.add(s);
        }
        for (Integer key : map.keySet()) {
            System.out.println(key + ":" + map.get(key));
        }



        Object canRunLock = new Object();
        ExecutorService threadPool = Executors.newFixedThreadPool(10);
        HashQueue<String, Task> hashQueue = new HashQueue<>(1000);

        // 每个 queueName 的并发度为 1
        Map<String, AtomicBoolean> runningFlags = new ConcurrentHashMap<>();

        // 消费者线程
        Thread consumerThread = new Thread(() -> {
            while (true) {
                try {
                    Task task = hashQueue.take(); // 阻塞获取
                    AtomicBoolean running = runningFlags.computeIfAbsent(task.queueName, k -> new AtomicBoolean(false));

                    // 当前 queue 已经在执行，则退回 shard 并重排
                    if (!running.compareAndSet(false, true)) {
                        hashQueue.addFirst(task.queueName, task);

                        // 所有分片队首，shardSize-1（遍历到当前task的上一个分片）
                        List<Task> allShardHeadTaskList = hashQueue.peek(1);
                        /**
                         * 所有分片队首有一个能执行，不能阻塞
                         * 所有分片队首全部无法执行，阻塞
                         */
                        boolean canRunInAllShardHeadTaskList = false;
                        for(Task tempTask : allShardHeadTaskList) {
                            AtomicBoolean tempRunningBoolean = runningFlags.computeIfAbsent(tempTask.queueName, k -> new AtomicBoolean(false));
                            if (!tempRunningBoolean.get()) {
                                canRunInAllShardHeadTaskList = true;
                                break;
                            }
                        }
                        if (!canRunInAllShardHeadTaskList) {
                            // 获取当前 shard 的队列副本
                            List<Task> shardTasks = hashQueue.getCopyShardValues(task.queueName);
                            boolean multipleQueues = false;
                            for (Task t : shardTasks) {
                                if (!t.queueName.equals(task.queueName)) {
                                    multipleQueues = true;
                                    break;
                                }
                            }
                            // 如果 shard 内有多个 queue，则把当前 queue 的任务移动到尾部
                            if (multipleQueues) {
                                hashQueue.moveMatchToEnd(task.queueName, t -> t.queueName.equals(task.queueName));
                            }
                            // 阻塞等待唤醒，避免空跑
                            synchronized (canRunLock) {
                                canRunLock.wait(); // 等待其他任务完成或生产者唤醒
                            }
                        }
                        continue; // 立即处理下一个任务
                    }

                    // 异步消费
                    threadPool.execute(() -> {
                        try {
                            System.out.println(Thread.currentThread().getName() + " consuming " + task);
                            // 模拟慢任务
                            if (task.queueName.startsWith("Q1")) {
                                Thread.sleep(1000 * 5);
                            } else {
                                Thread.sleep(50);
                            }
                            System.out.println("Finished " + task);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        } finally {
                            running.set(false);
                            synchronized (canRunLock) {
                                canRunLock.notify(); // 唤醒等待线程
                            }
                        }
                    });

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, "Consumer-Main");
        consumerThread.start();

        // 模拟生产者线程
        Thread producerThread = new Thread(() -> {
            List<String> queueList = Arrays.asList("Q1", "Q2", "Q3");
            Map<String, List<Task>> queue2taskListMap = new HashMap<>();
            for (int i = 0; i < queueList.size(); i++) {
                String q = queueList.get(i);
                for (int j = 0; j < 5; j++) {
                    try {
                        Task task = new Task(q, "msg-" + j);
                        List<Task> taskList = queue2taskListMap.computeIfAbsent(q, key -> new ArrayList<>());
                        taskList.add(task);
                        Thread.sleep(200); // 模拟生产速率
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            try {
                addHashQueue(hashQueue, queue2taskListMap, canRunLock);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            for (int i = 0; i < 20; i++) {
                Task task = new Task("Q4", "msg-" + i);
                try {
                    addHashQueue(hashQueue, task, canRunLock);
                    Thread.sleep(200); // 模拟生产速率
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

//            try {
//                Thread.sleep(1000 * 20); // 模拟生产速率
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            int count = 0;
//            while (true) {
//                Task task = new Task("Q5", "msg-" + count++);
//                try {
//                    hashQueue.put("Q5", task);
//                    Thread.sleep(200); // 模拟生产速率
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
        }, "Producer");
        producerThread.start();
    }

    private static void addHashQueue(HashQueue<String, Task> hashQueue, Task task, Object canRunLock) throws InterruptedException {
        hashQueue.put(task.queueName, task);
        synchronized (canRunLock) {
            canRunLock.notify(); // 唤醒等待线程
        }
    }

    private static void addHashQueue(HashQueue<String, Task> hashQueue, Map<String, List<Task>> queue2taskListMap, Object canRunLock) throws InterruptedException {
        hashQueue.put(queue2taskListMap);
        synchronized (canRunLock) {
            canRunLock.notify(); // 唤醒等待线程
        }
    }

}
