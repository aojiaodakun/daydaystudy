package com.hzk.java.jmm;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 原子性：不保证
 */
public class Test2 {

    private static volatile int index;

    private static volatile AtomicInteger atomicInteger = new AtomicInteger();

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            new Thread(()->{
                for (int j = 0; j < 2000; j++) {
                    index++;
                    atomicInteger.getAndIncrement();
                }
            }).start();
        }
        Thread.sleep(1000);
        System.out.println("整数:" + index);// ?<20000
        System.out.println("原子整形:" + atomicInteger.get()); // 20000
    }

}
