package com.hzk.consumer;

import com.hzk.factory.ConsumerFactory;
import com.hzk.service.IDemoService;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.utils.ReferenceConfigCache;

/**
 50、ReferenceConfig缓存
 */
public class ConsumerMain50 {

    public static void main(String[] args) throws Exception{
        // ReferenceConfig缓存
        ReferenceConfigCache referenceConfigCache = ReferenceConfigCache.getCache();

        // 服务引用1
        ReferenceConfig<IDemoService> referenceConfig1 = ConsumerFactory.getCommonReferenceConfig();
        IDemoService demoService1 = referenceConfigCache.get(referenceConfig1);

        // 服务引用2
        ReferenceConfig<IDemoService> referenceConfig2 = ConsumerFactory.getCommonReferenceConfig();
        IDemoService demoService2 = referenceConfigCache.get(referenceConfig2);
        System.out.println("引用对象是否相等：" + (demoService1 == demoService2));

        // 服务调用
        while (true) {
            String name = "hzk";
            System.out.println("remote invoke,param:" + name);
            demoService1.sayHello(name);
            Thread.currentThread().sleep(1000*3);
        }

    }

}
