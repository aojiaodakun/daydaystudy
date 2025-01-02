package com.hzk.consumer;

import com.hzk.service.DemoService;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;

import java.util.concurrent.CountDownLatch;

public class ConsumerConnectionsMain {

    public static void main(String[] args) throws Exception{
        new Thread(()->{
            try {
                consumer1();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(()->{
            try {
                consumer2();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        new CountDownLatch(1).await();
    }

    private static void consumer1() throws Exception{
        ReferenceConfig<DemoService> referenceConfig = new ReferenceConfig<>();
        referenceConfig.setInterface(DemoService.class);
        // 仅远程调用
        referenceConfig.setScope("remote");
        referenceConfig.setApplication(new ApplicationConfig("dubbo-demo-api-consumer"));
        referenceConfig.setTimeout(1000 * 20);
        RegistryConfig registryConfig = new RegistryConfig("zookeeper://127.0.0.1:2181");
        referenceConfig.setRegistry(registryConfig);
        referenceConfig.setCluster("failsafe");
        referenceConfig.setGroup("hzk1");
        referenceConfig.setVersion("1.0.0");
        referenceConfig.setConnections(1);
        // 服务引用
        DemoService demoService = referenceConfig.get();
        new Thread(()->{
            // 同步调用
            while (true) {
                try {
                    Thread.currentThread().sleep(1000 * 3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String message = demoService.sayHello("dubbo");
                System.out.println(message);
            }
        }).start();

        new CountDownLatch(1).await();
    }

    private static void consumer2() throws Exception{
        ReferenceConfig<DemoService> referenceConfig = new ReferenceConfig<>();
        referenceConfig.setInterface(DemoService.class);
        // 仅远程调用
        referenceConfig.setScope("remote");
        referenceConfig.setApplication(new ApplicationConfig("dubbo-demo-api-consumer"));
        referenceConfig.setTimeout(1000 * 20);
        RegistryConfig registryConfig = new RegistryConfig("zookeeper://127.0.0.1:2181");
        referenceConfig.setRegistry(registryConfig);
        referenceConfig.setCluster("failsafe");
        referenceConfig.setGroup("hzk1");
        referenceConfig.setVersion("1.0.0");
        referenceConfig.setConnections(1);
        // 服务引用
        DemoService demoService = referenceConfig.get();
        new Thread(()->{
            // 同步调用
            while (true) {
                try {
                    Thread.currentThread().sleep(1000 * 3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String message = demoService.sayHello("dubbo");
                System.out.println(message);
            }
        }).start();

        new CountDownLatch(1).await();
    }

}
