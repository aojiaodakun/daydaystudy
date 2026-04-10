package com.hzk.java.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DeadLockTest {

    public static void main(String[] args) throws Exception {
        synchronizedTest();
//        reentrantLockTest();

        System.in.read();
    }

    private static void synchronizedTest() throws Exception{
        Object object1 = new Object();
        Object object2 = new Object();

        new Thread(()->{
            synchronized (object1) {
                System.out.println("thread-A get object1");
                try {
                    Thread.currentThread().sleep(1000 * 5);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                synchronized (object2) {
                    System.out.println("thread-A get object2");
                }
            }
        }, "A").start();

        new Thread(()->{
            synchronized (object2) {
                System.out.println("thread-B get object2");
                try {
                    Thread.currentThread().sleep(1000 * 5);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                synchronized (object1) {
                    System.out.println("thread-B get object1");
                }
            }
        }, "B").start();
    }

    private static void reentrantLockTest() throws Exception{
        Lock lock1 = new ReentrantLock();
        Lock lock2 = new ReentrantLock();


        new Thread(()->{
            lock1.lock();
            System.out.println("thread-A get object1");
            try {
                Thread.currentThread().sleep(1000 * 5);
            } catch (Exception e) {
                e.printStackTrace();
            }
            lock2.lock();
            System.out.println("thread-A get object2");
        }, "A").start();

        new Thread(()->{
            lock2.lock();
            System.out.println("thread-B get object2");
            try {
                Thread.currentThread().sleep(1000 * 5);
            } catch (Exception e) {
                e.printStackTrace();
            }
            lock1.lock();
            System.out.println("thread-B get object1");
        }, "B").start();
    }


}
