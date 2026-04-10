package com.hzk.java.oom.thread;

/**
 * java.lang.OutOfMemoryError: unable to create new native thread
 * 在JDK8的内存模型中，线程栈内存（通过-Xss参数设置）既不属于Java堆内存，也不属于直接内存(Direct Memory)，而是属于JVM进程的本地内存(Native Memory)
 * JVM进程内存
 * ├── Java堆内存 (-Xms/-Xmx)
 * ├── 元空间 (Metaspace) - 本地内存
 * ├── 直接内存 (Direct Memory) - 本地内存
 * ├── 线程栈内存 (-Xss) - 本地内存
 * ├── JVM自身代码和数据 - 本地内存
 * └── 其他本地内存分配
 *
 * -Xms20m -Xmx20m -Xss1m -XX:+HeapDumpOnOutOfMemoryError -XX:+PrintGCDetails -XX:+PrintGCDateStamps
 */
public class TooManyThreadOOMTest1 {

    public static void main(String[] args) {
        // TODO 线程用本地内存，可能会导致操作系统宕机，谨慎操作
        int threadSize = 10000;
        System.out.println("Java堆内存最大: " + Runtime.getRuntime().maxMemory() / 1024 / 1024 + "MB");
        // 获取当前线程数
        int threads = 0;
        try {
            for (int i = 0; i < threadSize; i++) {
                new Thread(() -> {
                    try {
                        Thread.sleep(1000000);
                    } catch (InterruptedException e) {}
                }).start();
                threads++;
            }
        } catch (OutOfMemoryError e) {
            System.out.println("创建线程数: " + threads);
            System.out.println("错误: " + e.toString());
        }
    }

}

