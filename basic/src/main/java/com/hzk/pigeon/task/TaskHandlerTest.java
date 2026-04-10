package com.hzk.pigeon.task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.LockSupport;

public class TaskHandlerTest {

    private static ExecutorService threadPool = Executors.newFixedThreadPool(32);
    // 并发度
    private static Map<String, AtomicInteger> queue2concurrencyMap = new HashMap<>();


    public static void main(String[] args) {
        Object canRunLock = new Object();
        ListQueue<String, QueueDTO> taskQueue = new ListQueue<>();

        // 生产者线程
        for (int i = 1; i <= 20; i++) {
            String queueName = "q" + i;
            queue2concurrencyMap.put(queueName, new AtomicInteger(1));
            Map<String, List<QueueDTO>> queue2queueListMap = new HashMap<>();
            List<QueueDTO> queueList = new ArrayList<>(10000);
            for (int j = 1; j <= 10000; j++) {
                QueueDTO tempQueueDTO = new QueueDTO();
                tempQueueDTO.setQueueName(queueName);
                tempQueueDTO.setMsg("msg" + j);
                queueList.add(tempQueueDTO);
            }
            queue2queueListMap.put(queueName, queueList);
            taskQueue.put(queue2queueListMap);
        }
        taskQueue.size("q1");

        threadPool.execute(()->{
            while (true) {
                try {
                    Thread.currentThread().sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                for (int i = 1; i <= 20; i++) {
                    String queueName = "q" + i;
                    System.out.println(queueName + ",size=" + taskQueue.size(queueName));
                }
            }
        });

        // 消费者线程
        while (true) {
            try {
                QueueDTO queueDTO = taskQueue.take();
                String queueName = queueDTO.getQueueName();
                AtomicInteger concurrency = queue2concurrencyMap.get(queueName);
                if (concurrency.get() < 1) {
                    taskQueue.addFirst(queueName, queueDTO);
                    List<QueueDTO> allShardHeadTaskList = taskQueue.peekRounds(1);
                    boolean canRunInAllShardHeadTaskList = false;
                    for(QueueDTO tempQueueDTO : allShardHeadTaskList) {
                        AtomicInteger tempConcurrency = queue2concurrencyMap.get(tempQueueDTO.getQueueName());
                        if (tempConcurrency.get() > 0) {
                            canRunInAllShardHeadTaskList = true;
                            break;
                        }
                    }
                    if (!canRunInAllShardHeadTaskList) {
                        /**
                         * 全部分片的第一条消息没有并发度
                         * 阻塞等待唤醒，避免空跑
                         */
                        synchronized (canRunLock) {
                            canRunLock.wait();
                        }
//                        LockSupport.parkNanos(5000000);//休眠5ms

//                        System.out.println("main");
                    }
                    continue;
                }
                concurrency.decrementAndGet();
                threadPool.execute(() ->{
//                    System.out.println("consume message,queue=" + queueName + ",msg=" + queueDTO.getMsg());
                    try {
                        Thread.currentThread().sleep(100);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        concurrency.incrementAndGet();
                    }
                    synchronized (canRunLock) {
                        canRunLock.notify();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }




}
