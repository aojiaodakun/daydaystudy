package com.hzk.java.jmm;

import org.openjdk.jol.info.GraphLayout;

import java.util.concurrent.TimeUnit;

/**
 * 可见性
 */
public class Test1 {
    static MyData myData = new MyData();
    public static void main(String[] args) {
        System.out.println("main start");
        System.out.println(myData);
        //打印实例外部的占用
        System.out.println(GraphLayout.parseInstance(myData).toPrintable());
        System.out.println("-------------------------------------------");
        new Thread(()->{
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {}
            System.out.println("thread location:" + myData);
            myData.setNumber();
            myData.setName();
            System.out.println("number=" + myData.number + ",name = " + myData.name);
            //打印实例外部的占用
            System.out.println(GraphLayout.parseInstance(myData).toPrintable());
            System.out.println("-------------------------------------------");
        }).start();
        // 死循环
        while (myData.name.equals("a")) {
        }
        System.out.println("main end");
    }
}

class MyData{
    int number = 0;
    String name = "a";
    public void setNumber(){
        number = 60;
    }
    public void setName(){
        name = "b";
    }
}
