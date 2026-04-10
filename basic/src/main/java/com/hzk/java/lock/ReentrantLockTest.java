package com.hzk.java.lock;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockTest {

    public static void main(String[] args) throws Exception {
        Lock lock = new ReentrantLock();

        new Thread(()->{
            lockAndBizwork(lock);
        }, "A").start();

        new Thread(()->{
            lockAndBizwork(lock);
        }, "B").start();
//        Thread.currentThread().sleep(10000);
//        System.out.println("-------------优化-------------");

//        new Thread(()->{
//            bizwork1(lock);
//        }, "C").start();
//
//        new Thread(()->{
//            bizwork1(lock);
//        }, "D").start();

        System.in.read();
    }

    private static void lockAndBizwork(Lock lock){
        try {
            lock.lock();
            System.out.println(Thread.currentThread().getName() + ",dateTime=" + LocalDateTime.now() + ",get lock");
            // 业务，假定执行5秒
            Thread.currentThread().sleep(5000);
            System.out.println(Thread.currentThread().getName() + ",dateTime=" + LocalDateTime.now() + ",do bizwork success");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    private static void tryLockAndBizwork(Lock lock){
        boolean flag = false;
        try {
            flag = lock.tryLock(6, TimeUnit.SECONDS);
            if (flag) {
                System.out.println(Thread.currentThread().getName() + ",get lock");
                // 业务，假定执行3秒，3秒<6秒
                Thread.currentThread().sleep(3000);
                System.out.println(Thread.currentThread().getName() + ",do bizwork success");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (flag) {
                lock.unlock();
            }
        }
    }

    private static void bizwork1(Lock lock){
        boolean flag = lock.tryLock();// 差异点，TODO
        try {
            if (flag) {
                System.out.println(Thread.currentThread().getName() + ",get lock");
                // 业务，假定执行3秒，3秒<6秒
                Thread.currentThread().sleep(3000);
                System.out.println(Thread.currentThread().getName() + ",do bizwork");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (flag) {
                lock.unlock();
            }
        }
    }

}

