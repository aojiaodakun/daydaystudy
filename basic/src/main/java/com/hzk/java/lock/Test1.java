package com.hzk.java.lock;

import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Test1 {

    public static void main(String[] args) throws Exception{


        CountDownLatch countDownLatch = new CountDownLatch(1);

        new Thread(()->{
            try {
                Thread.currentThread().sleep(1000);
                countDownLatch.countDown();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        boolean await = countDownLatch.await(5, TimeUnit.SECONDS);

        int i = 4 %3;


        Date date = new Date(1668763697769L);
        Date date2 = new Date(1668763697168L);

//
        Lock lock = new ReentrantLock(true);
        lock.lock();

        Semaphore semaphore = new Semaphore(1);
        semaphore.acquire();


        new Thread(()->{
            try {
                semaphore.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        System.in.read();

//        lock.lockInterruptibly();

//        Thread.currentThread().interrupt();
//        System.out.println("stop");
        //lock.unlock();

//        Thread.sleep(1000);
        new Thread(()->{
            try {
                lock.lock();
//                lock.lockInterruptibly();
            } catch (Exception e) {
                e.printStackTrace();
            }
            lock.unlock();
        },"t1").start();

//        Thread.sleep(1000);
        new Thread(()->{
            lock.lock();

            lock.unlock();
        },"t2").start();

    }




}
