package com.hzk.java.thread;

/**
 * 仅可用于新线程，线程池不可用
 */
public class InheritableThreadLocalTest {

    public static InheritableThreadLocal<String> LOCAL_NAME = new InheritableThreadLocal<>();


    public static void main(String[] args) throws Exception {
        LOCAL_NAME.set("hzk");
        new Thread(() -> {
            String localName = LOCAL_NAME.get();
            System.out.println(localName);
        }).start();
        System.in.read();
    }


}
