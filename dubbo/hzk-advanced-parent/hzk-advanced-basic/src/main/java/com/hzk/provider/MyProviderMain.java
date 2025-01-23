package com.hzk.provider;

import com.hzk.constants.HzkCommonConstants;
import com.hzk.service.DispatchService;
import com.hzk.service.DispatchServiceImpl;
import com.hzk.service.IDemoService;
import com.hzk.service.impl.CommonDemoServiceImpl;
import com.hzk.service.impl.PwhDemoServiceImpl;
import org.apache.dubbo.config.*;

import java.util.concurrent.CountDownLatch;

public class MyProviderMain {

    public static void main(String[] args) throws Exception {
        ServiceConfig<DispatchService> serviceConfig = new ServiceConfig<>();
        serviceConfig.setInterface(DispatchService.class);
        // 服务
        serviceConfig.setRef(new DispatchServiceImpl());
        // 协议配置
        serviceConfig.setProtocol(new ProtocolConfig(HzkCommonConstants.PROTOCOL_DUBBO));
        // 提供者配置
        serviceConfig.setProvider(new ProviderConfig());
        // 应用配置
        serviceConfig.setApplication(new ApplicationConfig(HzkCommonConstants.APPLICATION_NAME_PROVIDER));
        // 注册配置
        serviceConfig.setRegistry(new RegistryConfig(HzkCommonConstants.REGISTRY_ZK_2181));
        serviceConfig.export();
        System.out.println("服务导出完成");
        // 阻塞主线程
        new CountDownLatch(1).await();
    }

}
