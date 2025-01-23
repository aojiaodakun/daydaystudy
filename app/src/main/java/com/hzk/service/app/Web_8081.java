package com.hzk.service.app;

import com.hzk.service.bootstrap.Booter;

public class Web_8081 {

    public static void main(String[] args) throws Exception {
        System.setProperty("appName", "web");
        System.setProperty("JETTY_PORT", "8081");
        System.setProperty("dubbo.protocol.port", "20881");
        Booter.main(args);
    }

}
