package com.hzk.lock;

/**
 * 使用ReentrantLock编写死锁demo
 */
public class DLockTest {

    public static void main(String[] args) {
        new Thread(()->{

        }, "A").start();


        new Thread(()->{

        }, "B").start();
    }

}
