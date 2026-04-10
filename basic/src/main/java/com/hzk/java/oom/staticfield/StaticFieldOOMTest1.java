package com.hzk.java.oom.staticfield;

import com.hzk.java.oom.vo.OomObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * java.lang.OutOfMemoryError: Java heap space
 * 类变量泄露导致的OOM
 * -Xms20m -Xmx20m -XX:+HeapDumpOnOutOfMemoryError -XX:+PrintGCDetails -XX:+PrintGCDateStamps
 */
public class StaticFieldOOMTest1 {

    private static List<String> userIdList = new ArrayList<>(30);

    private static Map<String, OomObject> userId_session_map = new ConcurrentHashMap<>(32);

    public static void main(String[] args) {
        method1();
    }

    private static void method1(){
        // 申请25M堆内存
        for (int i = 0; i < 25; i++) {
            userIdList.add(i+"");
            userId_session_map.put(i + "", new OomObject());
        }
        System.out.println("method1 end");
    }


}
