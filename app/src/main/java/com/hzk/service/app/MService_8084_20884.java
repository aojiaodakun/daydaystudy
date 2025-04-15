package com.hzk.service.app;

import com.hzk.service.bootstrap.Booter;

public class MService_8084_20884 {

    public static void main(String[] args) throws Exception {
        System.setProperty("appName", "mservice");
        System.setProperty("JETTY_PORT", "8084");
        System.setProperty("dubbo.protocol.port", "20884");
        Booter.main(args);
    }

}
