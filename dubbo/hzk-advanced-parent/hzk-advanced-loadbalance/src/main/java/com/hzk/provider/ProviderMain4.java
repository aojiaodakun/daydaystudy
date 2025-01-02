package com.hzk.provider;

import com.hzk.factory.ProviderFactory;
import org.apache.dubbo.config.ServiceConfig;

import java.util.concurrent.CountDownLatch;

/**
 4、负载均衡
 */
public class ProviderMain4 {

    public static void main(String[] args) throws Exception{
        ServiceConfig serviceConfig = ProviderFactory.getCommonServiceConfig();
        // 服务导出
        serviceConfig.export();
        System.out.println("服务导出完成");
        // 阻塞主线程
        new CountDownLatch(1).await();
    }

}
