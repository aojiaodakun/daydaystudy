package com.hzk.test;

public class ShutdownHookTest {

    public static void main(String[] args) throws Exception{
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                System.out.println("ShutdownHook execute");
            }
        });


        new Thread(()->{
            try {
                Thread.currentThread().sleep(1000 * 10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("jvm exit");
            System.exit(5);
        }).start();



        System.in.read();
    }


}
