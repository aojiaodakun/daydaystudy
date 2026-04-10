///*
// * Main.java - main class for the Hello MBean and QueueSampler MXBean example.
// * Create the Hello MBean and QueueSampler MXBean, register them in the platform
// * MBean server, then wait forever (or until the program is interrupted).
// */
//
//package com.hzk.java.jmx.mbean.dynamic;
//
//import com.sun.jdmk.comm.HtmlAdaptorServer;
//
//import java.lang.management.ManagementFactory;
//import java.util.Queue;
//import java.util.concurrent.ArrayBlockingQueue;
//import javax.management.MBeanServer;
//import javax.management.ObjectName;
//
///**
// * https://blog.csdn.net/wuliusir/article/details/49301493
// */
//public class DynamicMBeanTest {
//
//    public static void main(String[] args) throws Exception {
//        MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
//        ObjectName helloName = new ObjectName("com.hzk:type=hello");
//        HelloDynamic hello = new HelloDynamic();
//        mBeanServer.registerMBean(hello, helloName);
//        ObjectName adapterName = new ObjectName("HelloAgent:name=htmladapter,port=8082");
//        HtmlAdaptorServer adapter = new HtmlAdaptorServer();
//        mBeanServer.registerMBean(adapter, adapterName);
//        adapter.start();
//        System.out.println("start.....");
//
//        Thread.sleep(Long.MAX_VALUE);
//    }
//}
