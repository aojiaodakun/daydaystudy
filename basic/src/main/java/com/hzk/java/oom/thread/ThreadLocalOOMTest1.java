package com.hzk.java.oom.thread;

import com.hzk.java.oom.vo.OomObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 线程ThreadLocal大对象导致OOM
 * -Xms20m -Xmx20m -XX:+HeapDumpOnOutOfMemoryError -XX:+PrintGCDetails -XX:+PrintGCDateStamps
 */
public class ThreadLocalOOMTest1 {

    private static ThreadLocal<List<OomObject>> oomObjectListLocal = new ThreadLocal<>();

    public static void main(String[] args) {
        method1();
    }

    private static void method1(){
        initThreadLocal();
        // 申请25M堆内存
        for (int i = 0; i < 25; i++) {
            oomObjectListLocal.get().add(new OomObject());
        }
        System.out.println("method1 end");
    }

    private static void initThreadLocal() {
        List<OomObject> list = new ArrayList<>();
        oomObjectListLocal.set(list);
    }

}
