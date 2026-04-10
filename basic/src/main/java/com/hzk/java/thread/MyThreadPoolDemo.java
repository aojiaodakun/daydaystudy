package com.hzk.java.thread;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class MyThreadPoolDemo {

    /**
     * 1、核心线程数
     * 2、最大线程池
     * 3、保留时间数值
     * 4、保留时间单位
     * 5、阻塞队列
     * 6、线程池工厂
     * 7、拒绝策略
     */
    private static ExecutorService executorService = new ThreadPoolExecutor(1, 3, 1L, TimeUnit.SECONDS, new SynchronousQueue<>()
            , new ThreadFactory() {
        AtomicInteger integer = new AtomicInteger(0);
        @Override
        public Thread newThread(Runnable runnable) {
            return new Thread(runnable, "hzk_thread_" + integer.getAndIncrement());
        }
    }, new ThreadPoolExecutor.AbortPolicy());


    public static void main(String[] args) throws Exception {
        ReentrantLock reentrantLock = new ReentrantLock();


        executorService.execute(() -> {
            try {
                System.out.println("111");
                TimeUnit.SECONDS.sleep(Integer.MAX_VALUE);
            } catch (Exception e) {

            }
        });
        TimeUnit.SECONDS.sleep(2);

        executorService.execute(() -> {
            try {
                System.out.println("222");
                TimeUnit.SECONDS.sleep(Integer.MAX_VALUE);
            } catch (Exception e) {

            }
        });
        TimeUnit.SECONDS.sleep(2);
        executorService.execute(() -> {
            try {
                System.out.println("333");
                TimeUnit.SECONDS.sleep(Integer.MAX_VALUE);
            } catch (Exception e) {

            }
        });
        TimeUnit.SECONDS.sleep(2);
        executorService.execute(() -> {
            try {
                System.out.println("444");
                TimeUnit.SECONDS.sleep(Integer.MAX_VALUE);
            } catch (Exception e) {

            }
        });
        TimeUnit.SECONDS.sleep(2);
        executorService.execute(() -> {
            try {
                System.out.println("555");
                TimeUnit.SECONDS.sleep(Integer.MAX_VALUE);
            } catch (Exception e) {

            }
        });

    }

}
