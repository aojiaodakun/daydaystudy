package com.hzk.factory;

import com.hzk.constants.HzkCommonConstants;
import com.hzk.service.IDemoService;
import com.hzk.service.impl.CommonDemoServiceImpl;
import org.apache.dubbo.config.*;

public class ProviderFactory {

    public static ServiceConfig getCommonServiceConfig(){
        ServiceConfig<IDemoService> serviceConfig = new ServiceConfig<>();
        serviceConfig.setInterface(IDemoService.class);
        // 服务
        serviceConfig.setRef(new CommonDemoServiceImpl());
        // 协议配置
        serviceConfig.setProtocol(new ProtocolConfig(HzkCommonConstants.PROTOCOL_DUBBO));
        // 提供者配置
        serviceConfig.setProvider(new ProviderConfig());
        // 应用配置
        serviceConfig.setApplication(new ApplicationConfig(HzkCommonConstants.APPLICATION_NAME_PROVIDER));
        // 注册配置
        serviceConfig.setRegistry(new RegistryConfig(HzkCommonConstants.REGISTRY_ZK_2181));
        return serviceConfig;
    }

}
