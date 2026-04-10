package com.hzk.java.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class ThreadTest {
    static ThreadPoolExecutor executorService = (ThreadPoolExecutor)Executors.newFixedThreadPool(1);


    public static void main(String[] args) throws Exception{
        for (int i = 0; i < 10; i++) {
            executorService.execute(()->{
                try {
                    System.out.println(Thread.currentThread().getName() + " start");
                    Thread.currentThread().sleep(1000 * 1);
                    System.out.println(Thread.currentThread().getName() + " end");
                } catch (Exception e) {

                }
            });
        }
        System.in.read();

//        Thread.currentThread().sleep(1000 * 5);
//        executorService.setCorePoolSize(3);
//        executorService.setMaximumPoolSize(5);
//        System.out.println("do");

//        ThreadTest threadTest = new ThreadTest();
//        threadTest.invoke();
//        threadTest.invoke1();
    }




    public void invoke() throws Exception {
        List<ThreadVO> list = new ArrayList<>();
        ThreadVO threadVO = new ThreadVO();
        threadVO.method1();
        list.add(threadVO);

        Thread.sleep(5000);
        list.remove(0);

        Thread.sleep(30000);
    }

    public void invoke1() throws Exception {
        System.out.println("invoke1");
    }


}
