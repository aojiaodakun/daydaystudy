package com.hzk.java.lock;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SynchronizedTest {

    private static ExecutorService executorService = Executors.newFixedThreadPool(20);


    public static void main(String[] args) throws Exception{
        test2();




        System.in.read();
    }


    private static void test1() throws Exception {
        Object object = new Object();
        new Thread(()->{
            try {
                Thread.currentThread().sleep(1000 * 5);
                synchronized (object){
                    System.out.println("notify before");
                    object.notify();
                    Thread.currentThread().sleep(1000 * 10);
                    System.out.println("notify after");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
        synchronized (object){
            System.out.println("wait before");
//            Thread.currentThread().sleep(1000 * 60);
            object.wait();
            System.out.println("wait after");
        }
    }

    private static void test2() throws Exception {
        System.out.println("start");
        CountDownLatch countDownLatch = new CountDownLatch(20);
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 20; i++) {
            executorService.execute(() ->{
                int max = 100 * 100 * 1;// 1万
                int index = 0;
                while (max > index) {
//                    normalMethod();
                    syncMethod();
                    index++;
                }
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();
        long endTime = System.currentTimeMillis();
        System.out.println("end,cost=" + (endTime - startTime) + "ms");
    }

    public static synchronized void syncMethod() {
        try {
            Thread.currentThread().sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int i = 1;
    }

    public static void normalMethod() {
        try {
            Thread.currentThread().sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int i = 1;
    }

}
