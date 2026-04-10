package com.hzk.java.jmx.mbean.standard;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;

public class StandardMBeanTest {

    public static void main(String[] args) throws Exception {
        MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
        ObjectName name = new ObjectName("com.hzk:type=Hello");
        HelloService mbean = new HelloService();
        mBeanServer.registerMBean(mbean, name);

        System.in.read();
    }

}
