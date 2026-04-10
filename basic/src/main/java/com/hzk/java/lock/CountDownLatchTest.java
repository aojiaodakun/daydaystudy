package com.hzk.java.lock;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class CountDownLatchTest {

    public static void main(String[] args) throws Exception{
        int size = 6;
        CountDownLatch countDownLatch = new CountDownLatch(size);
        for (int i = 1; i <= size; i++) {
            int code = i;
            new Thread(()->{
                try {
                    String name = Thread.currentThread().getName();
                    String country = CountryEnum.getCountry(code).getCountry();
                    System.out.println(name + "," + country + "国被灭");
                }finally {
                    countDownLatch.countDown();
                }
            },String.valueOf(i)).start();
        }
        countDownLatch.await();
        System.out.println("秦国统一六国");
    }

}
