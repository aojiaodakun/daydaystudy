package com.hzk.consumer;

import com.hzk.factory.ConsumerFactory;
import com.hzk.service.IDemoService;
import org.apache.dubbo.config.ReferenceConfig;

import java.util.concurrent.CountDownLatch;

/**
 * 1、启动时检查
 * 7、只订阅
 * 30、延迟暴露
 * 33、延迟连接
 * 46、注册信息简化
 */
public class ConsumerMain1 {


    public static void main(String[] args) throws Exception {
        ReferenceConfig<IDemoService> referenceConfig = ConsumerFactory.getCommonReferenceConfig();
//        /**
//         * 1、启动时检查
//         */
        referenceConfig.getRegistry().setCheck(Boolean.FALSE);
        referenceConfig.setTimeout(1000 * 60 * 5);
//        /**
//         * 7、只订阅
//         */
//        referenceConfig.getRegistry().setRegister(Boolean.TRUE);
        /**
         * 33、延迟连接
         */
//        referenceConfig.setLazy(true);
        referenceConfig.setGroup("bos");
        // 服务引用
        IDemoService demoService = null;
        try {
            demoService = referenceConfig.get();
        } catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("服务引用完成");

        String name = "hzk";
//        demoService.sayHello(name);
        // 服务调用
        while (true) {
            System.out.println("remote invoke,param:" + name);
            demoService.sayHello(name);
            Thread.currentThread().sleep(1000*3);
        }

    }

}
