package com.hzk.consumer;

import com.hzk.constants.HzkCommonConstants;
import com.hzk.service.DispatchService;
import com.hzk.service.IDemoService;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;

public class MyConsumerMain {


    public static void main(String[] args) throws Exception {
        ReferenceConfig<DispatchService> referenceConfig = new ReferenceConfig<>();
        referenceConfig.setInterface(DispatchService.class);
        // 应用配置
        referenceConfig.setApplication(new ApplicationConfig(HzkCommonConstants.APPLICATION_NAME_CONSUMER));
        // 注册配置
        referenceConfig.setRegistry(new RegistryConfig(HzkCommonConstants.REGISTRY_ZK_2181));

        // 服务引用
        DispatchService dispatchService = null;
        try {
            dispatchService = referenceConfig.get();
        } catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("服务引用完成");
        // 服务调用
        String subString = dispatchService.invoke("com.hzk.service.ReflectVO", "hello", new String[]{"java.lang.String"}, new Object[]{"hzk"});
        System.out.println(1);

    }
}
