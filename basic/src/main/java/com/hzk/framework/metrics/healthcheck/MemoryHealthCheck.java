package com.hzk.framework.metrics.healthcheck;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.codahale.metrics.health.HealthCheck;
import com.codahale.metrics.health.HealthCheckRegistry;
import org.junit.Test;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryUsage;
import java.lang.management.OperatingSystemMXBean;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 内存占用率检查
 * 场景：
 * 主线程阻塞，分线程一直申请内存不释放
 * 启动时虚拟机参数：-Xmx100m
 */
public class MemoryHealthCheck extends HealthCheck {

    private final static HealthCheckRegistry REGISTRY = new HealthCheckRegistry();

    public MemoryHealthCheck(){
        REGISTRY.register(MemoryHealthCheck.class.getName(), this);
    }

    @Override
    protected Result check() throws Exception {
        int jvmMemoryRatio = getJvmMemoryRatio();
        if (jvmMemoryRatio < 80) {
            return HealthCheck.Result.healthy();
        } else {
            return HealthCheck.Result.unhealthy("jvm memory used to much");
        }
    }

    private static Runnable thread = new Runnable() {
        int time = 0;
        List<Byte[]> list = new ArrayList<>();
        @Override
        public void run() {
            for (;;) {
                time++;
                System.out.println("第" + time + "次申请5M内存");
                // 申请5M内存
                Byte[] bytes = new Byte[1024 * 1024 * 5];
                list.add(bytes);
                try {
                    Thread.currentThread().sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };


    @Test
    public void testMemory() throws Exception{
        // 申请内存
        new Thread(()->{
            int time = 0;
            List<Byte[]> list = new ArrayList<>();
            for (;;) {
                time++;
                System.out.println("第" + time + "次申请5M内存");
                // 申请5M内存
                Byte[] bytes = new Byte[1024 * 1024 * 5];
                list.add(bytes);
                try {
                    Thread.currentThread().sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "memoryThread").start();

        // 健康检查
        new Thread(()->{
            for (;;) {
                Result result = REGISTRY.runHealthCheck(MemoryHealthCheck.class.getName());
                System.out.println("headthCheckResult:" + result);
                try {
                    Thread.currentThread().sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "healthCheckThread").start();

        System.in.read();
    }

    private int getJvmMemoryRatio(){
        MemoryUsage heapMemoryUsage = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage();
        // 返回Java虚拟机最初从操作系统请求进行内存管理的以字节为单位的内存量
        System.out.println("jvm.heap.init is " + heapMemoryUsage.getInit());
        // 以字节为单位返回使用的内存量
        System.out.println("jvm.heap.used is " + heapMemoryUsage.getUsed());
        // 返回为Java虚拟机提供的内存量（以字节计）。
        System.out.println("jvm.heap.committed is " + heapMemoryUsage.getCommitted());
        // 返回可用于内存管理的最大内存量（以字节为单位）
        System.out.println("jvm.heap.max is " + heapMemoryUsage.getMax());
        int memoryRatio = (int)((heapMemoryUsage.getUsed()/(double)heapMemoryUsage.getMax()) * 100);
        System.out.println("内存占比 = " + memoryRatio);
        System.out.println("------------------------------------");
        return memoryRatio;
    }

    /**
     * 获取操作系统指标
     * @return 操作系统指标
     */
    private Map<String, Double> getOsSystemMetricMap(){
        long gb = 1024 * 1024 * 1024;
        OperatingSystemMXBean operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();
        JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(operatingSystemMXBean));
        double processCpuLoad = jsonObject.getDouble("processCpuLoad") * 100;
        double systemCpuLoad = jsonObject.getDouble("systemCpuLoad") * 100;
        long totalPhysicalMemorySize = jsonObject.getLong("totalPhysicalMemorySize") * 100;
        long freePhysicalMemorySize = jsonObject.getLong("freePhysicalMemorySize") * 100;
        double totalMemory = 1.0 * totalPhysicalMemorySize / gb;
        double freeMemory = 1.0 * freePhysicalMemorySize / gb;
        double memoryUseRatio = 1.0 * (totalPhysicalMemorySize - freePhysicalMemorySize) / totalPhysicalMemorySize * 100;

        StringBuilder sb = new StringBuilder();
        sb.append("系统CPU占用率:")
                .append(twoDecimal(systemCpuLoad))
                .append("%,内存占用率:")
                .append(twoDecimal(memoryUseRatio))
                .append("%,系统总内存:")
                .append(twoDecimal(totalMemory))
                .append("GB,系统剩余内存:")
                .append(twoDecimal(freeMemory))
                .append("GB,该进程占用CPU:")
                .append(twoDecimal(processCpuLoad))
                .append("%");
        System.out.println(sb.toString());
        Map<String, Double> map = new HashMap<>();
        map.put("systemCpuLoad", twoDecimal(systemCpuLoad));
        map.put("memoryUseRatio", twoDecimal(memoryUseRatio));
        map.put("totalMemory", twoDecimal(totalMemory));
        map.put("freeMemory", twoDecimal(freeMemory));
        map.put("processCpuLoad", twoDecimal(processCpuLoad));
        return map;
    }

    private static double twoDecimal(double doubleValue) {
        BigDecimal bigDecimal = new BigDecimal(doubleValue).setScale(2, RoundingMode.HALF_UP);
        return bigDecimal.doubleValue();
    }

}
