package com.hzk.factory;

import com.hzk.constants.HzkCommonConstants;
import com.hzk.service.IDemoService;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;

public class ConsumerFactory {

    public static ReferenceConfig<IDemoService> getCommonReferenceConfig(){
        ReferenceConfig<IDemoService> referenceConfig = new ReferenceConfig<>();
        referenceConfig.setInterface(IDemoService.class);
        // 应用配置
        referenceConfig.setApplication(new ApplicationConfig(HzkCommonConstants.APPLICATION_NAME_CONSUMER));
        // 注册配置
        referenceConfig.setRegistry(new RegistryConfig(HzkCommonConstants.REGISTRY_ZK_2181));
        return referenceConfig;
    }

}
