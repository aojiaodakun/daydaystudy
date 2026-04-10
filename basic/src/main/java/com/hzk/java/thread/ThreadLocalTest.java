package com.hzk.java.thread;

public class ThreadLocalTest {


    public static ThreadLocal<Integer> LOCAL_TIMEOUT = new ThreadLocal<>();


    public static void main(String[] args) throws Exception {
        LOCAL_TIMEOUT.set(1);
        Integer integer = LOCAL_TIMEOUT.get();
        System.out.println(integer);
    }


}
