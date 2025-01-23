package com.hzk.dubbo.factory;

import com.hzk.constants.HzkCommonConstants;
import com.hzk.dubbo.service.DispatchService;
import com.hzk.service.IDemoService;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;

import static com.hzk.constants.HzkCommonConstants.PROTOCOL_DUBBO_DEFAULT_PORT;

public class ConsumerFactory {

    public static ReferenceConfig<DispatchService> getCommonReferenceConfig(){
        ReferenceConfig<DispatchService> referenceConfig = new ReferenceConfig<>();
        referenceConfig.setInterface(DispatchService.class);
//        referenceConfig.setGroup("bos");
        referenceConfig.setScope("remote");
        referenceConfig.setLoadbalance("mydiv");
        // 应用配置
        referenceConfig.setApplication(new ApplicationConfig(HzkCommonConstants.APPLICATION_NAME_CONSUMER));
        // 注册配置
        referenceConfig.setRegistry(new RegistryConfig(HzkCommonConstants.REGISTRY_ZK_2181));
        referenceConfig.setVersion("1.0.0");
        referenceConfig.setTimeout(1000 * 30);
        return referenceConfig;
    }

}
