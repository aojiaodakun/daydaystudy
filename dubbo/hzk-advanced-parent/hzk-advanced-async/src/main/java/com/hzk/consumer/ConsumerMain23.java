package com.hzk.consumer;

import com.hzk.constants.HzkCommonConstants;
import com.hzk.factory.ConsumerFactory;
import com.hzk.service.IDemoService;
import com.hzk.service.IDemoService23;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.MethodConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.rpc.RpcContext;

import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 23、异步执行
 24、异步调用
 */
public class ConsumerMain23 {

    private static IDemoService23 demoService;

    public static void main(String[] args) throws Exception{
        ReferenceConfig<IDemoService23> referenceConfig = new ReferenceConfig<>();
        referenceConfig.setInterface(IDemoService23.class);
        // 应用配置
        referenceConfig.setApplication(new ApplicationConfig(HzkCommonConstants.APPLICATION_NAME_CONSUMER));
        // 注册配置
        referenceConfig.setRegistry(new RegistryConfig(HzkCommonConstants.REGISTRY_ZK_2181));
        referenceConfig.setTimeout(1000 * 10);

        // asyncCall2的前提
        MethodConfig methodConfig = new MethodConfig();
        methodConfig.setName("asyncCall2");
        methodConfig.setAsync(true);
        referenceConfig.setMethods(Arrays.asList(methodConfig));
        // 服务引用
        demoService = referenceConfig.get();
        System.out.println("服务引用完成");
        // 服务调用
        while (true) {
            /**
             * 24、异步调用
             */
//            asyncCall1();
            asyncCall2();
//            asyncCall3();
            Thread.currentThread().sleep(1000*3);
        }

    }


    private static void asyncCall1(){
        System.out.println("---------------------------------");
        String taskId = UUID.randomUUID().toString().substring(0, 8);
        // 异步调用1：调用直接返回CompletableFuture
        CompletableFuture<String> future = demoService.asyncCall1("async call request");
        // 增加回调
        future.whenComplete((v, t) -> {
            if (t != null) {
                t.printStackTrace();
            } else {
                System.out.println("taskId: " + taskId + ",Response: " + v);
            }
        });
        // 早于结果输出
        System.out.println("taskId: " + taskId + ",Executed before response return.");
    }

    private static void asyncCall2(){
        System.out.println("---------------------------------");
        String taskId = UUID.randomUUID().toString().substring(0, 8);
        // 异步调用2
        demoService.asyncCall2("async");// 返回null
        // 拿到调用的Future引用，当结果返回后，会被通知和设置到此Future
        CompletableFuture<String> helloFuture = RpcContext.getContext().getCompletableFuture();
        // 为Future添加回调
        helloFuture.whenComplete((retValue, exception) -> {
            if (exception == null) {
                System.out.println("taskId: " + taskId + ",result:" + retValue);
            } else {
                exception.printStackTrace();
            }
        });
        // 早于结果输出
        System.out.println("taskId: " + taskId + ",Executed before response return.");
    }

    private static void asyncCall3(){
        System.out.println("---------------------------------");
        String taskId = UUID.randomUUID().toString().substring(0, 8);
        // 异步调用3
        CompletableFuture<String> future3 = RpcContext.getContext().asyncCall(()->{
            return demoService.sayHello("async call request");
        });
        // do other things
        try {
            String result = future3.get();
            System.out.println("taskId: " + taskId + ",result:" + result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
