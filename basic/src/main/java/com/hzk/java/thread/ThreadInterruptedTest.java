package com.hzk.java.thread;

public class ThreadInterruptedTest {

    public static void main(String[] args) throws Exception {
        Thread t1 = new Thread(()->{
            System.out.println("t1 start");
            try {
                Thread.currentThread().sleep(1000 * 60);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        t1.start();

        Thread.currentThread().sleep(1000 * 5);
        t1.interrupt();
        System.in.read();
    }

}
