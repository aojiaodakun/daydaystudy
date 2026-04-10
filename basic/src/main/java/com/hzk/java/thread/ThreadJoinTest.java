package com.hzk.java.thread;

public class ThreadJoinTest {

    public static void main(String[] args) throws Exception{

        Thread thread1 = new Thread(() -> {
            System.out.println("aa start");
            try {
                Thread.currentThread().sleep(1000 * 5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("aa end");
        });
        thread1.start();
        thread1.join();

        System.out.println("main");
        System.in.read();
    }

}
