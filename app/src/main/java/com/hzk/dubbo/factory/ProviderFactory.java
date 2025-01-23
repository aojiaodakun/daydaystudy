package com.hzk.dubbo.factory;

import com.hzk.constants.HzkCommonConstants;
import com.hzk.dubbo.service.DispatchService;
import com.hzk.dubbo.service.DispatchServiceImpl;
import com.hzk.service.IDemoService;
import com.hzk.service.impl.CommonDemoServiceImpl;
import org.apache.dubbo.config.*;

public class ProviderFactory {

    public static ServiceConfig getCommonServiceConfig(){
        ServiceConfig<DispatchService> serviceConfig = new ServiceConfig<>();
        serviceConfig.setInterface(DispatchService.class);
        // 服务
        serviceConfig.setRef(new DispatchServiceImpl());
        // 协议配置
        serviceConfig.setProtocol(new ProtocolConfig(HzkCommonConstants.PROTOCOL_DUBBO));
        // 提供者配置
        serviceConfig.setProvider(new ProviderConfig());
        serviceConfig.setVersion("1.0.0");
        // 应用配置
        serviceConfig.setApplication(new ApplicationConfig(HzkCommonConstants.APPLICATION_NAME_PROVIDER));
        // 注册配置
        serviceConfig.setRegistry(new RegistryConfig(HzkCommonConstants.REGISTRY_ZK_2181));
        return serviceConfig;
    }

}
