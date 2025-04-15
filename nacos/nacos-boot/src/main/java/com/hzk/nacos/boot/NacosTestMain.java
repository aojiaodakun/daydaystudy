package com.hzk.nacos.boot;

import com.alibaba.nacos.api.config.ConfigService;
import com.hzk.nacos.v2.NacosFactory;

import java.util.Properties;

public class NacosTestMain {

    public static void main(String[] args) throws Exception{
        // 读配置文件
        Properties p = new Properties();
        p.load(NacosTestMain.class.getResourceAsStream("/config.properties"));
        System.out.println(p);
        String configUrl = System.getProperty("configUrl", p.getProperty("configUrl"));
        System.out.println("configUrl=" + configUrl);
        // 配置服务
        System.out.println("---------------configServiceStart----------------");
        ConfigService nacosConfigClient = NacosFactory.getNacosConfigClient(configUrl);
        String dataId = System.getProperty("dataId", p.getProperty("dataId"));
        System.out.println("dataId=" + dataId);
        String group = System.getProperty("group", p.getProperty("group"));
        System.out.println("group=" + group);
        System.out.println(nacosConfigClient.getConfig(dataId, group, 30000));
        System.out.println("---------------configServiceEnd----------------");
        System.out.println("-----------------------------------------------");

        // 注册服务
        System.out.println("---------------registryServiceStart----------------");
//        NacosNamingService nacosNamingService = NacosFactory.getNacosNamingService(configUrl);


        System.out.println("---------------registryServiceEnd----------------");
    }

}
