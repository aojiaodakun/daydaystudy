package com.hzk.nacos.config;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.client.config.utils.SnapShotSwitch;

import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

/**
 * 配置格式：
 * dataId集合：
 * 1、common.yaml,mservice,yaml
 * 2、common.yaml,web,yaml
 *
 * group：mservice，common，web
 *
 * prop，conf等作为dataId
 *
 * 主目标：
 * mq，redis配置，包含IP的配置项
 * log.config单独一个dataId
 *
 * 认证：https://nacos.io/zh-cn/docs/v2/guide/user/auth.html
 */
public class NacosConfigTest {

    static String dataId = "prop.properties";
    static String group = "common";
    static String serverAddr = "http://127.0.0.1:8848";
    static String username = "nacos";
    static String password = "nacos12";

    static Map<String, String> concurrentHashMap = new ConcurrentHashMap<>();


    public static void main(String[] args) throws Exception{
        // 关闭本地存储
        SnapShotSwitch.setIsSnapShot(false);
        // 关闭云环境配置
        System.setProperty("nacos.use.cloud.namespace.parsing", "false");
        Properties properties = new Properties();
        properties.put(PropertyKeyConst.SERVER_ADDR, serverAddr);
        properties.put(PropertyKeyConst.USERNAME, username);
        properties.put(PropertyKeyConst.PASSWORD, password);
        ConfigService configService = NacosFactory.createConfigService(properties);
        String value = "a=1\n" +
                "b=2";
        // 写
        configService.publishConfig(dataId, group, value, "properties");


        // 读
        String config1 = configService.getConfig("test1", "DEFAULT_GROUP", 5000);
//        String config2 = configService.getConfig("test1", "DEFAULT_GROUP", "mservice", "mservice", 5000);
        System.out.println(config1);

        System.err.println("---------------------------");
        // 监听
        configService.addListener(dataId, group, new Listener() {
            @Override
            public void receiveConfigInfo(String configInfo) {
                System.out.println("recieve:\n" + configInfo);
                System.err.println("---------------------------");
            }

            @Override
            public Executor getExecutor() {
                return null;
            }
        });

        String newValue = "a=1\n" +
                "b=2\n" +
                "c=3";
        configService.publishConfig(dataId, group, newValue, "properties");

        System.in.read();
    }

}
