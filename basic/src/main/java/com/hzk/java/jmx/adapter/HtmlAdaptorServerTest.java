//package com.hzk.java.jmx.adapter;
//
//import com.hzk.java.jmx.mbean.standard.HelloService;
//import com.sun.jdmk.comm.HtmlAdaptorServer;
//
//import javax.management.MBeanServer;
//import javax.management.ObjectName;
//import java.lang.management.ManagementFactory;
//
//public class HtmlAdaptorServerTest {
//
//    public static void main(String[] args) throws Exception {
//        MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
//        ObjectName name = new ObjectName("com.hzk:type=Hello");
//        HelloService mbean = new HelloService();
//        mBeanServer.registerMBean(mbean, name);
//
//        // 浏览器访问localhost:8082
//        ObjectName adapterName = new ObjectName("HelloAgent:name=htmladapter,port=8082");
//        // 注册工具页面访问适配器，也是个MBean
//        HtmlAdaptorServer adapter = new HtmlAdaptorServer();
//        mBeanServer.registerMBean(adapter, adapterName);
//        adapter.start();
//
//        System.in.read();
//    }
//
//}
