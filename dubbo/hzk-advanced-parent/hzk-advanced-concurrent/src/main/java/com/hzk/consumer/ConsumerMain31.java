package com.hzk.consumer;

import com.hzk.constants.ConcurrentConstants;
import com.hzk.factory.ConsumerFactory;
import com.hzk.service.IDemoService;
import org.apache.dubbo.config.ReferenceConfig;

import java.util.concurrent.CountDownLatch;

/**
 31、并发控制
 */
public class ConsumerMain31 {

    public static void main(String[] args) throws Exception{
        ReferenceConfig<IDemoService> referenceConfig = ConsumerFactory.getCommonReferenceConfig();
        referenceConfig.setTimeout(1000 * 10);

        // 测试时，切换com.hzk.constants.ConcurrentConstants.CONCURRENT_CONTROL
        if (ConcurrentConstants.CONCURRENT_CONTROL.equals(ConcurrentConstants.CONCURRENT_CONTROL_CONSUMER) ||
                ConcurrentConstants.CONCURRENT_CONTROL.equals(ConcurrentConstants.CONCURRENT_CONTROL_ALL)) {
            // 消费者并发控制
            referenceConfig.setActives(1);
        }
        // 服务引用
        IDemoService demoService = referenceConfig.get();
        System.out.println("服务引用完成");

        // 并发控制测试
        int concurrent = 5;
        for (int i = 0; i < concurrent; i++) {
            new Thread(()->{
                // 服务调用
                while (true) {
                    String threadName = Thread.currentThread().getName();
                    System.out.println("remote invoke,param:" + threadName);
                    try {
                        demoService.sayHello(threadName);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        Thread.currentThread().sleep(1000*3);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }, "hzk_" + i).start();
        }

        // 阻塞主线程
        new CountDownLatch(1).await();

    }

}
