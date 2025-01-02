package com.hzk.provider;

import com.hzk.factory.ProviderFactory;
import org.apache.dubbo.config.ProviderConfig;
import org.apache.dubbo.config.ServiceConfig;

import java.util.concurrent.CountDownLatch;

/**
 11、静态服务
 */
public class ProviderMain11 {

    public static void main(String[] args) throws Exception{
        ServiceConfig serviceConfig = ProviderFactory.getCommonServiceConfig();
        /**
         * 11、静态服务
         * 人工管理服务提供者的上线和下线，不建议使用
         * order:1、ProviderConfig；2、ServiceConfig；
         */
        // 11-1
        serviceConfig.getProvider().setDynamic(false);
        // 11-2
        serviceConfig.setDynamic(false);
        // 服务导出
        serviceConfig.export();
        System.out.println("服务导出完成");
        // 阻塞主线程
        new CountDownLatch(1).await();
    }

}
