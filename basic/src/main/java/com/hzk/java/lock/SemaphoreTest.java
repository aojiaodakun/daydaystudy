package com.hzk.java.lock;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * 3个车位，6个车抢车位
 */
public class SemaphoreTest {

    public static void main(String[] args) {
        Semaphore semaphore = new Semaphore(3);
        for (int i = 1; i <= 6; i++) {
            new Thread(()->{
                try {
                    semaphore.acquire();
                    System.out.println("可用信号量:" + semaphore.availablePermits());
                    System.out.println(Thread.currentThread().getName() + " 抢到了车位");
                    System.out.println("-------------------------");
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    semaphore.release();
                    System.out.println(Thread.currentThread().getName() + " 离开了车位");
                    System.out.println("-------------------------");
                }
            },String.valueOf(i)).start();
        }
    }

}
