package com.hzk.provider;

import com.hzk.factory.ProviderFactory;
import org.apache.dubbo.config.ServiceConfig;

import java.util.concurrent.CountDownLatch;

/**
 12、多版本
 不同版本不同互通
 */
public class ProviderMain12 {

    public static void main(String[] args) throws Exception{
        ServiceConfig serviceConfig = ProviderFactory.getCommonServiceConfig();
        /**
         * 12、多版本
         */
        serviceConfig.setVersion("1.0.0");
        // 服务导出
        serviceConfig.export();
        System.out.println("服务导出完成");
        // 阻塞主线程
        new CountDownLatch(1).await();
    }

}
