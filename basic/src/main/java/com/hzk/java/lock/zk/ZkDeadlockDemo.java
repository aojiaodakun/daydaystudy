package com.hzk.java.lock.zk;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingDeque;

public class ZkDeadlockDemo {
    static final LinkedBlockingDeque<String> outgoingQueue = new LinkedBlockingDeque<>();

    public static void main(String[] args) throws Exception {
        // 确保pollFirst() 能立即返回
        outgoingQueue.add("init");

        CountDownLatch aGotMonitor = new CountDownLatch(1);
        CountDownLatch bGotReentrant = new CountDownLatch(1);

        // 模拟应用线程（queuePacket）
        Thread appThread = new Thread(() -> {
            synchronized (outgoingQueue) {
                System.out.println("AppThread: got monitor lock");
                aGotMonitor.countDown(); // 通知：我拿到 monitor 了

                try {
                    // 等发送线程拿到 ReentrantLock
                    bGotReentrant.await();
                } catch (InterruptedException ignored) {}

                System.out.println("AppThread: try addLast (need ReentrantLock)");
                outgoingQueue.addLast("app"); // 会卡住
                System.out.println("AppThread: finished addLast");
            }
        }, "AppThread");

        // 模拟发送线程（SendThread）
        Thread sendThread = new Thread(() -> {
            try {
                System.out.println("SendThread: try pollFirst (need ReentrantLock)");
                outgoingQueue.pollFirst(); // 内部会拿 ReentrantLock
                System.out.println("SendThread: got ReentrantLock via pollFirst");
                bGotReentrant.countDown(); // 通知：我拿到 ReentrantLock 了

                aGotMonitor.await(); // 等 appThread 拿到 monitor

                System.out.println("SendThread: try enter synchronized(outgoingQueue)");
                synchronized (outgoingQueue) {
                    System.out.println("SendThread: got monitor lock");
                }
            } catch (InterruptedException ignored) {}
        }, "SendThread");

        appThread.start();
        sendThread.start();

        appThread.join();
        sendThread.join();
    }
}
