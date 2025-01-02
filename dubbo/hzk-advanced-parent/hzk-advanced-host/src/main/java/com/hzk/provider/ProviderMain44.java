package com.hzk.provider;

import com.hzk.factory.ProviderFactory;
import org.apache.dubbo.config.ServiceConfig;

import java.util.concurrent.CountDownLatch;

/**
 44、主机绑定
 */
public class ProviderMain44 {

    public static void main(String[] args) throws Exception{
        ServiceConfig serviceConfig = ProviderFactory.getCommonServiceConfig();
        // 使用自己本机网口IP
        serviceConfig.getProtocol().setHost("192.168.147.1");
        // 服务导出
        serviceConfig.export();
        System.out.println("服务导出完成");
        // 阻塞主线程
        new CountDownLatch(1).await();
    }

}
