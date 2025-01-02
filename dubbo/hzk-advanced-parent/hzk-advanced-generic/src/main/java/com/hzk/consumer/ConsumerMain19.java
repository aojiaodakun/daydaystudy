package com.hzk.consumer;

import com.hzk.constants.HzkCommonConstants;
import com.hzk.factory.ConsumerFactory;
import com.hzk.service.IDemoService;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.rpc.service.GenericService;

/**
 19、实现泛化调用
 */
public class ConsumerMain19 {

    public static void main(String[] args) throws Exception{
        ReferenceConfig<GenericService> referenceConfig = new ReferenceConfig<>();
        // 弱类型接口名
        referenceConfig.setInterface("com.hzk.service.IDemoService");
        // 声明为泛化服务
        referenceConfig.setGeneric("true");
        // 应用配置
        referenceConfig.setApplication(new ApplicationConfig(HzkCommonConstants.APPLICATION_NAME_CONSUMER));
        // 注册配置
        referenceConfig.setRegistry(new RegistryConfig(HzkCommonConstants.REGISTRY_ZK_2181));
        // 服务引用
        GenericService genericService = referenceConfig.get();
        System.out.println("服务引用完成");
        // 服务调用
        while (true) {
            String name = "hzk";
            System.out.println("remote invoke,param:" + name);
            Object result = genericService.$invoke("sayHello", new String[]{String.class.getName(), String.class.getName()}, new Object[]{name});
            System.out.println(result);
            Thread.currentThread().sleep(1000*3);
        }

    }

}
