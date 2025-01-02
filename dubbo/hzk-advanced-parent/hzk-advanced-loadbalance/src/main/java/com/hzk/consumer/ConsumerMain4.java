package com.hzk.consumer;

import com.hzk.factory.ConsumerFactory;
import com.hzk.service.IDemoService;
import org.apache.dubbo.config.ReferenceConfig;

/**
 4、负载均衡
 */
public class ConsumerMain4 {

    public static void main(String[] args) throws Exception{
        ReferenceConfig<IDemoService> referenceConfig = ConsumerFactory.getCommonReferenceConfig();
        /**
         * 4、负载均衡
         * 默认random，即RandomLoadBalance
         * 注：启动时以消费侧为准，运行时以配置中心为准
         * 获取到provider解析时，本地url覆盖远程url
         * org.apache.dubbo.registry.integration.RegistryDirectory#toInvokers(java.util.List)
         *      URL url = mergeUrl(providerUrl);
         * 使用时
         * org.apache.dubbo.rpc.cluster.support.AbstractClusterInvoker#invoke(org.apache.dubbo.rpc.Invocation)
         */
        // 最少活跃，即LeastActiveLoadBalance
        referenceConfig.setLoadbalance("leastactive");
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
