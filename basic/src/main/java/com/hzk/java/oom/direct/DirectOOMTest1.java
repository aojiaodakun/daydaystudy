package com.hzk.java.oom.direct;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
/**
 * java.lang.OutOfMemoryError: Direct buffer memory
 * 直接内存OOM
 * -Xms20m -Xmx20m -XX:MaxDirectMemorySize=20m -XX:NativeMemoryTracking=summary
 */
public class DirectOOMTest1 {

    public static void main(String[] args) {
        List<ByteBuffer> bufferList = new ArrayList<>();
        long totalAllocated = 0;// 总分配直接内存
        int allocationCount = 0;// 次数
        // 1M的字节数
        final int bytesPerMB = 1024 * 1024;
        // 每次分配的内存大小(MB, 默认10)
        int mbPerAllocation = 1;
        int bufferSize = mbPerAllocation * bytesPerMB;

        try {
            while (true) {
                // 分配直接内存1M
                ByteBuffer buffer = ByteBuffer.allocateDirect(bufferSize);
                // 写入数据以确保内存实际被分配
                for (int i = 0; i < bufferSize; i += 4096) {
                    buffer.put(i, (byte) (i % 256));
                }
                bufferList.add(buffer);


                totalAllocated += mbPerAllocation;
                allocationCount++;

                // 打印分配信息
                if (allocationCount % 10 == 0) {
                    System.out.printf("已分配: %d MB, 分配次数: %d%n", totalAllocated, allocationCount);
                    // 显示内存信息
                    printMemoryStats();
                }
            }
        } catch (OutOfMemoryError e) {
            System.err.println("==================================");
            System.err.println("发生OutOfMemoryError!");
            System.err.println("总分配量: " + totalAllocated + " MB");
            System.err.println("分配次数: " + allocationCount);
            System.err.println("错误信息: " + e.getMessage());
            System.err.println("==================================");

            // 清理内存
            bufferList.clear();
            System.gc();

            try {
                Thread.sleep(2000); // 等待GC完成
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
            printMemoryStats();
        }
    }

    private static void printMemoryStats() {
        // 获取内存信息
        long maxMemory = Runtime.getRuntime().maxMemory() / (1024 * 1024);
        long totalMemory = Runtime.getRuntime().totalMemory() / (1024 * 1024);
        long freeMemory = Runtime.getRuntime().freeMemory() / (1024 * 1024);
        long usedHeapMemory = totalMemory - freeMemory;

        System.out.println("堆内存统计:");
        System.out.printf("最大堆内存: %d MB%n", maxMemory);
        System.out.printf("已分配堆内存: %d MB%n", totalMemory);
        System.out.printf("已使用堆内存: %d MB%n", usedHeapMemory);
        System.out.println("----------------------------------");
    }
}
