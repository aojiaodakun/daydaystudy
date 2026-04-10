package com.hzk.java.oom.heap;

import com.hzk.java.oom.vo.OomObject;

import java.util.ArrayList;
import java.util.List;

/**
 * java.lang.OutOfMemoryError: Java heap space
 * 堆内存OOM，线程栈创建了大对象
 * -Xms20m -Xmx20m -XX:+HeapDumpOnOutOfMemoryError -XX:+PrintGCDetails -XX:+PrintGCDateStamps
 */
public class HeapOOMTest1 {


    public static void main(String[] args) {
        method1();
    }

    private static void method1(){
        // 申请25M堆内存
        List<OomObject> list = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
            list.add(new OomObject());
        }
        System.out.println("method1 end");
    }

}
