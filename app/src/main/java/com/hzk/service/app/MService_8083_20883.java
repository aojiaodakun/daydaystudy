package com.hzk.service.app;

import com.hzk.service.bootstrap.Booter;

public class MService_8083_20883 {

    public static void main(String[] args) throws Exception {
        System.setProperty("appName", "mservice");
        System.setProperty("JETTY_PORT", "8083");
        System.setProperty("dubbo.protocol.port", "20883");
        Booter.main(args);
    }

}
