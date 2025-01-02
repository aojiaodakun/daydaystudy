package com.hzk.provider;

import com.hzk.factory.ProviderFactory;
import com.hzk.service.IDemoService23;
import com.hzk.service.impl.DemoServiceImpl23;
import org.apache.dubbo.config.ServiceConfig;

import java.util.concurrent.CountDownLatch;

/**
 23、异步执行
 24、异步调用
 */
public class ProviderMain23 {

    public static void main(String[] args) throws Exception{
        ServiceConfig serviceConfig = ProviderFactory.getCommonServiceConfig();
        serviceConfig.setInterface(IDemoService23.class);
        serviceConfig.setRef(new DemoServiceImpl23());
        // 服务导出
        serviceConfig.export();
        System.out.println("服务导出完成");
        // 阻塞主线程
        new CountDownLatch(1).await();
    }

}
