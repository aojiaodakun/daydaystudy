package com.hzk.java.jmm;

/**
 * 有序性
 */
public class ReSortDemo {

    int a = 0;
    volatile boolean flag = false;


    public void method1(){

        a = 1;// 语句1
        flag = true;// 语句2
    }


    public void method2(){
        if(flag){
            a = a + 5;// 语句3
            System.out.println("value:" + a);
        }
    }

}
