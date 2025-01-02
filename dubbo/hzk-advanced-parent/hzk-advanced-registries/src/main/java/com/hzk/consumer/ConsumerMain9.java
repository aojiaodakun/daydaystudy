package com.hzk.consumer;

import com.hzk.constants.HzkCommonConstants;
import com.hzk.factory.ConsumerFactory;
import com.hzk.service.IDemoService;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;

import java.util.Arrays;
import java.util.List;

/**
 9、多注册中心
 */
public class ConsumerMain9 {

    public static void main(String[] args) throws Exception{
        ReferenceConfig<IDemoService> referenceConfig = ConsumerFactory.getCommonReferenceConfig();
        /**
         * 9、多注册中心
         */
        RegistryConfig registryConfig1 = referenceConfig.getRegistry();
        RegistryConfig registryConfig2 = new RegistryConfig(HzkCommonConstants.REGISTRY_ZK_2182);
        List<RegistryConfig> registryConfigList = Arrays.asList(registryConfig1, registryConfig2);
        referenceConfig.setRegistries(registryConfigList);
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
