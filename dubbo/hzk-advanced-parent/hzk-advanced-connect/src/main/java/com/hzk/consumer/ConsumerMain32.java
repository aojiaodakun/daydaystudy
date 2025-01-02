package com.hzk.consumer;

import com.hzk.constants.ConnectConstants;
import com.hzk.factory.ConsumerFactory;
import com.hzk.service.IDemoService;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.rpc.RpcContext;

import java.util.concurrent.CountDownLatch;

/**
 32、连接控制
 */
public class ConsumerMain32 {

    public static void main(String[] args) throws Exception{
        ReferenceConfig<IDemoService> referenceConfig = ConsumerFactory.getCommonReferenceConfig();
        referenceConfig.setTimeout(1000 * 10);

        // 测试时，切换com.hzk.constants.ConnectConstants.CONNECT_CONTROL
        if (ConnectConstants.CONNECT_CONTROL.equals(ConnectConstants.CONNECT_CONTROL_CONSUMER) ||
                ConnectConstants.CONNECT_CONTROL.equals(ConnectConstants.CONNECT_CONTROL_ALL)) {
            // 连接控制
            referenceConfig.setConnections(4);
        }
        // 服务引用
        IDemoService demoService = referenceConfig.get();
        System.out.println("服务引用完成");

        // 连接控制测试
        int concurrent = 5;
        for (int i = 0; i < concurrent; i++) {
            new Thread(()->{
                // 服务调用
                while (true) {
                    try {
                        String threadName = Thread.currentThread().getName();
                        demoService.sayHello(threadName);
                        System.out.println("remote invoke,param:" + threadName);
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
