package com.hzk.consumer;

import com.hzk.factory.ConsumerFactory;
import com.hzk.service.IDemoService;
import org.apache.dubbo.config.ReferenceConfig;

/**
 3、集群容错
 */
public class ConsumerMain3 {

    public static void main(String[] args) throws Exception{
        ReferenceConfig<IDemoService> referenceConfig = ConsumerFactory.getCommonReferenceConfig();
        /**
         * 3、集群容错
         * 默认为failover，即FailoverClusterInvoker
         * 仅读取消费者侧配置
         * org.apache.dubbo.registry.integration.RegistryProtocol#refer(java.lang.Class, org.apache.dubbo.common.URL)
         * Cluster cluster = Cluster.getCluster(qs.get(CLUSTER_KEY));
         */
        referenceConfig.setCluster("failsafe");
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
