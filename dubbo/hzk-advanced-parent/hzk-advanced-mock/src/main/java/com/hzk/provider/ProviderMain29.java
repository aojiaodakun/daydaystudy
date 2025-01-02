package com.hzk.provider;

import com.hzk.factory.ProviderFactory;
import com.hzk.service.IDemoService29;
import com.hzk.service.impl.DemoServiceImpl29;
import org.apache.dubbo.config.ServiceConfig;

import java.util.concurrent.CountDownLatch;

/**
 29、本地伪装
 41、服务降级
 */
public class ProviderMain29 {

    public static void main(String[] args) throws Exception{
        ServiceConfig serviceConfig = ProviderFactory.getCommonServiceConfig();
        serviceConfig.setInterface(IDemoService29.class);
        serviceConfig.setRef(new DemoServiceImpl29());
        // 服务导出
        serviceConfig.export();
        System.out.println("服务导出完成");
        // 阻塞主线程
        new CountDownLatch(1).await();
    }

}
