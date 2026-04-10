package com.hzk.java.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadVO {

    ExecutorService executor = Executors.newFixedThreadPool(2);

//    ExecutorService executor = Executors.newFixedThreadPool(10, new ThreadFactory() {
//        AtomicInteger integer = new AtomicInteger(0);
//        @Override
//        public Thread newThread(Runnable r) {
//            return new Thread("ThreadVO_" + integer.getAndIncrement());
//        }
//    });


    public ThreadVO(){



    }

    public void method1(){
        executor.execute(()->{
            System.out.println("work1 start");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("work1 end");
        });
        executor.execute(()->{
            System.out.println("work2 start");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("work2 end");
        });
        executor.execute(()->{
            System.out.println(1111);
        });
    }


}
