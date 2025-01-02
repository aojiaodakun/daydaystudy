package com.hzk.provider;

import com.hzk.constants.HzkCommonConstants;
import com.hzk.factory.ProviderFactory;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.ServiceConfig;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 9、多注册中心
 */
public class ProviderMain9 {

    public static void main(String[] args) throws Exception{
        ServiceConfig serviceConfig = ProviderFactory.getCommonServiceConfig();
        /**
         * 9、多注册中心
         */
        RegistryConfig registryConfig1 = serviceConfig.getRegistry();
        RegistryConfig registryConfig2 = new RegistryConfig(HzkCommonConstants.REGISTRY_ZK_2182);
        List<RegistryConfig> registryConfigList = Arrays.asList(registryConfig1, registryConfig2);
        serviceConfig.setRegistries(registryConfigList);
        // 服务导出
        serviceConfig.export();
        System.out.println("服务导出完成");
        // 阻塞主线程
        new CountDownLatch(1).await();
    }

}
