package com.hzk.consumer;

import com.hzk.factory.ConsumerFactory;
import com.hzk.service.IDemoService;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.rpc.service.EchoService;

/**
 20、回声测试
 */
public class ConsumerMain20 {

    public static void main(String[] args) throws Exception{
        ReferenceConfig<IDemoService> referenceConfig = ConsumerFactory.getCommonReferenceConfig();
        // 服务引用
        IDemoService demoService = referenceConfig.get();
        System.out.println("服务引用完成");
        EchoService echoService = (EchoService)demoService;
        // 服务调用
        while (true) {
            System.out.println("echo test");
            /**
             * 20、回声测试
             */
            Object result = echoService.$echo("OK");
            System.out.println(result);
            Thread.currentThread().sleep(1000*3);
        }

    }

}
