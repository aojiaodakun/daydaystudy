package com.hzk.service.app;

import com.hzk.service.bootstrap.Booter;

public class MService_8082_20882 {

    public static void main(String[] args) throws Exception {
        System.setProperty("appName", "mservice");
        System.setProperty("JETTY_PORT", "8082");
        System.setProperty("dubbo.protocol.port", "20882");
        Booter.main(args);
    }

}
