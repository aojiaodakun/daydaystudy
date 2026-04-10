package com.hzk.java.lock;

import java.util.concurrent.CyclicBarrier;

/**
 * 收集7颗龙珠召唤神龙
 */
public class CyclicBarrierTest {

    public static void main(String[] args) {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(7,()->{
            System.out.println("召唤神龙");
        });
        for (int i = 1; i <= 7; i++) {
            int code = i;
            new Thread(()->{
                System.out.println(Thread.currentThread().getName() + " 收集到 " + code + " 星珠");
                try {
                    cyclicBarrier.await();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            },String.valueOf(i)).start();
        }
    }

}
