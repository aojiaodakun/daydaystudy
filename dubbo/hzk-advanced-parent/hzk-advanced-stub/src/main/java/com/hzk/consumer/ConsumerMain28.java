package com.hzk.consumer;

import com.hzk.factory.ConsumerFactory;
import com.hzk.service.IDemoService;
import com.hzk.stub.DemoServiceStub;
import org.apache.dubbo.config.ReferenceConfig;

/**
 28、本地存根
 */
public class ConsumerMain28 {

    public static void main(String[] args) throws Exception{
        ReferenceConfig<IDemoService> referenceConfig = ConsumerFactory.getCommonReferenceConfig();
        /**
         * 28、本地存根测试
         */
        referenceConfig.setStub(DemoServiceStub.class.getName());
        // 服务引用
        IDemoService demoService = referenceConfig.get();
        System.out.println("服务引用完成");
        // 服务调用
        while (true) {
            String name = "hzk";
            System.out.println("remote invoke,param:" + name);
            demoService.sayHello(name);
            Thread.currentThread().sleep(1000*3);
        }

    }

}
