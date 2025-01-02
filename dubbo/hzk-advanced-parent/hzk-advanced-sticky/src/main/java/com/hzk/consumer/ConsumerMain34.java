package com.hzk.consumer;

import com.hzk.factory.ConsumerFactory;
import com.hzk.service.IDemoService;
import org.apache.dubbo.config.ReferenceConfig;

/**
 34、粘滞连接
 org.apache.dubbo.rpc.cluster.support.AbstractClusterInvoker#select(org.apache.dubbo.rpc.cluster.LoadBalance, org.apache.dubbo.rpc.Invocation, java.util.List, java.util.List)
 */
public class ConsumerMain34 {

    public static void main(String[] args) throws Exception{
        ReferenceConfig<IDemoService> referenceConfig = ConsumerFactory.getCommonReferenceConfig();
        // 开启粘滞连接
        referenceConfig.setSticky(Boolean.TRUE);
        IDemoService demoService = referenceConfig.get();

        String name = "hzk";
        // 服务调用
        while (true) {
            System.out.println("remote invoke,param:" + name);
            try {
                System.out.println(demoService.sayHello(name));
                System.out.println("---------------------------");
            } catch (Exception e) {
                e.printStackTrace();
            }
            Thread.currentThread().sleep(1000*3);
        }

    }

}
