package com.hzk.provider;

import com.hzk.factory.ProviderFactory;
import com.hzk.service.IDemoService21;
import com.hzk.service.impl.DemoServiceImpl21;
import org.apache.dubbo.config.ServiceConfig;

import java.util.concurrent.CountDownLatch;

/**
 21、上下文信息
 22、隐式参数
 */
public class ProviderMain21 {

    public static void main(String[] args) throws Exception{
        ServiceConfig serviceConfig = ProviderFactory.getCommonServiceConfig();
        serviceConfig.setInterface(IDemoService21.class);
        serviceConfig.setRef(new DemoServiceImpl21());
        // 服务导出
        serviceConfig.export();
        System.out.println("服务导出完成");
        // 阻塞主线程
        new CountDownLatch(1).await();
    }

}
