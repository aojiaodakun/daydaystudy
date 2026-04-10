package com.hzk.java.oom.array;

/**
 * java.lang.OutOfMemoryError: Requested array size exceeds VM limit
 * 尝试分配超过Integer.MAX_VALUE-8的数组
 * -Xms20m -Xmx20m -XX:+HeapDumpOnOutOfMemoryError -XX:+PrintGCDetails -XX:+PrintGCDateStamps
 */
public class ArrayOOMTest1 {

    public static void main(String[] args) {
        byte[] arr = new byte[Integer.MAX_VALUE]; // 直接触发
    }

}
