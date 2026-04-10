package com.hzk.java.oom.gc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * java.lang.OutOfMemoryError: GC overhead limit exceeded
 * GC overhead limit exceeded是Java虚拟机在检测到垃圾回收效率极低时抛出的一种OOM。当JVM花费超过98%的时间进行垃圾回收，但只回收不到2%的堆空间时，就会触发这个错误。
 * -Xmx50m -XX:+UseParallelGC -XX:+HeapDumpOnOutOfMemoryError -XX:+PrintGCDetails -XX:+PrintGCTimeStamps
 */
public class GCOverheadOOMTest1 {

    public static void main(String[] args) {
        System.out.println("GC Overhead Limit Exceeded OOM模拟开始");
        System.out.println("==================================");
        List<Map<String, String>> list = new ArrayList<>();
        int counter = 0;
        try {
            while (true) {
                // 创建大量短期存活对象
                Map<String, String> tempMap = new HashMap<>();
                for (int i = 0; i < 100; i++) {
                    tempMap.put("key-" + i, "value-" + System.currentTimeMillis());
                }
                list.add(tempMap);
                // 定期清理部分引用，模拟部分对象存活
                if (++counter % 1000 == 0) {
                    list.subList(0, 500).clear(); // 清除部分引用
                    System.out.println("已添加 " + counter + " 个map，当前列表大小: " + list.size());
                }
            }
        } catch (Throwable t) {
            System.err.println("发生错误: " + t.toString());
            t.printStackTrace();
        }
    }
}