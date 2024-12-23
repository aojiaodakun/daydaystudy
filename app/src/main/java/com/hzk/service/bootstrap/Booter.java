package com.hzk.service.bootstrap;

import org.apache.dubbo.common.extension.ExtensionLoader;

public class Booter {

    public static void main(String[] args) throws Exception {
        // 1、获取配置中心configUrl的配置 TODO

        /**
         * 2、启动webServer
         */
        String defaultWebServerType = "jetty";
//        String defaultWebServerType = "tomcat";
        String webServerType = System.getProperty("webserver.type", defaultWebServerType);
        // dubboSpi，先学习javaSpi
        BootServer bootServer = ExtensionLoader.getExtensionLoader(BootServer.class).getExtension(webServerType);
        bootServer.start(args);

        /**
         * 3、启动monitor，TODO
         */

    }

}
