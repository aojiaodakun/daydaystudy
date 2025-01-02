package com.hzk.provider;

import com.hzk.factory.ProviderFactory;
import com.hzk.service.IDemoService13;
import com.hzk.service.impl.DemoServiceImpl13_1;
import com.hzk.service.impl.DemoServiceImpl13_2;
import org.apache.dubbo.config.*;

import java.util.concurrent.CountDownLatch;

/**
 13、分组聚合
 */
public class ProviderMain13 {

    public static void main(String[] args) throws Exception{
        new Thread(()->{
            ServiceConfig<IDemoService13> serviceConfig = ProviderFactory.getCommonServiceConfig();
            serviceConfig.setInterface(IDemoService13.class);
            serviceConfig.setRef(new DemoServiceImpl13_1());
            // 分组
            serviceConfig.setGroup("app1");
            // 服务导出
            serviceConfig.export();
            System.out.println("服务1导出完成");
        }, "server1").start();
        new Thread(()->{
            ServiceConfig<IDemoService13> serviceConfig = ProviderFactory.getCommonServiceConfig();
            serviceConfig.setInterface(IDemoService13.class);
            serviceConfig.setRef(new DemoServiceImpl13_2());
            // 分组
            serviceConfig.setGroup("app2");
            // 服务导出
            serviceConfig.export();
            System.out.println("服务2导出完成");
        }, "server2").start();

        // 阻塞主线程
        new CountDownLatch(1).await();
    }

}
