package com.hzk.provider;

import com.hzk.factory.ConsumerFactory;
import com.hzk.factory.ProviderFactory;
import com.hzk.service.IDemoService;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.ServiceConfig;


/**
 25、本地调用
 */
public class ProviderConsumerMain25 {

    public static void main(String[] args) throws Exception{
        ServiceConfig serviceConfig = ProviderFactory.getCommonServiceConfig();
        // 服务导出
        serviceConfig.export();
        System.out.println("服务导出完成");

        // 服务引用
        ReferenceConfig<IDemoService> referenceConfig = ConsumerFactory.getCommonReferenceConfig();
        referenceConfig.setScope("injvm");
        IDemoService demoService = referenceConfig.get();
        System.out.println("服务引用完成");

        String name = "hzk";
        // 服务调用
        while (true) {
            String threadName = Thread.currentThread().getName();
            boolean isMainThread = threadName.equals("main");
            System.out.println("injvm invoke,param:" + name + ",isMainThread:" + isMainThread);
            demoService.sayHello(name);
            System.out.println("----------------------------");
            Thread.currentThread().sleep(1000*3);
        }
    }

}
