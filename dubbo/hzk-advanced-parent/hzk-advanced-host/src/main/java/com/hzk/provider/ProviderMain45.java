package com.hzk.provider;

import com.hzk.factory.ProviderFactory;
import org.apache.dubbo.config.ServiceConfig;

import java.util.concurrent.CountDownLatch;

/**
 45、主机配置
 */
public class ProviderMain45 {

    public static void main(String[] args) throws Exception{
        ServiceConfig serviceConfig = ProviderFactory.getCommonServiceConfig();
        /**
         * 45、主机配置
         * 此特性常用于容器环境，开放宿主机端口1，端口1将数据转发至jvm绑定的端口2
         * 本地测试:
         * 1、需开启nginx，配置tcp转发。监听9001，代理9002
         * 2、DUBBO_IP_TO_REGISTRY和DUBBO_IP_TO_BIND保持一致
         * 3、DUBBO_PORT_TO_REGISTRY为nginx监听端口，DUBBO_PORT_TO_BIND为tcp代理端口
         */
        // 注册中心配置
        System.setProperty("DUBBO_IP_TO_REGISTRY", "192.168.147.1");
        System.setProperty("DUBBO_PORT_TO_REGISTRY", "9001");
        // 通信协议配置
        System.setProperty("DUBBO_IP_TO_BIND", "192.168.147.1");
        System.setProperty("DUBBO_PORT_TO_BIND", "9002");

        // 服务导出
        serviceConfig.export();
        System.out.println("服务导出完成");
        // 阻塞主线程
        new CountDownLatch(1).await();
    }

}
