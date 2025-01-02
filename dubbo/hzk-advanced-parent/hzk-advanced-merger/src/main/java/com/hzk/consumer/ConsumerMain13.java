package com.hzk.consumer;

import com.hzk.constants.HzkCommonConstants;
import com.hzk.factory.ConsumerFactory;
import com.hzk.service.IDemoService;
import com.hzk.service.IDemoService13;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;

/**
 13、分组聚合
 */
public class ConsumerMain13 {

    public static void main(String[] args) throws Exception{
        ReferenceConfig<IDemoService13> referenceConfig = new ReferenceConfig<>();
        referenceConfig.setInterface(IDemoService13.class);
        // 应用配置
        referenceConfig.setApplication(new ApplicationConfig(HzkCommonConstants.APPLICATION_NAME_CONSUMER));
        // 注册配置
        referenceConfig.setRegistry(new RegistryConfig(HzkCommonConstants.REGISTRY_ZK_2181));
        // 分组聚合
        referenceConfig.setGroup("app1,app2");
        referenceConfig.setMerger("true");
        // 服务引用
        IDemoService13 demoService = referenceConfig.get();
        System.out.println("服务引用完成");
        // 服务调用
        while (true) {
            String name = "hzk";
            System.out.println("remote invoke,param:" + name);
            System.out.println("response: " + demoService.merger(name));
            Thread.currentThread().sleep(1000*3);
        }

    }

}
