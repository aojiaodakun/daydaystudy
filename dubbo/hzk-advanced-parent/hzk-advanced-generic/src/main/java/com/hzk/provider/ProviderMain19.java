package com.hzk.provider;

import com.hzk.constants.HzkCommonConstants;
import com.hzk.factory.ProviderFactory;
import com.hzk.service.IDemoService;
import com.hzk.service.impl.CommonDemoServiceImpl;
import com.hzk.service.impl.MyGenericServiceImpl;
import org.apache.dubbo.config.*;
import org.apache.dubbo.rpc.service.GenericService;

import java.util.concurrent.CountDownLatch;

/**
 19、实现泛化调用
 */
public class ProviderMain19 {

    public static void main(String[] args) throws Exception{
        ServiceConfig<GenericService> serviceConfig = new ServiceConfig<>();
        // 服务
        serviceConfig.setRef(new MyGenericServiceImpl());
        // 弱类型接口名
        serviceConfig.setInterface(IDemoService.class.getName());
        // 协议配置
        serviceConfig.setProtocol(new ProtocolConfig(HzkCommonConstants.PROTOCOL_DUBBO));
        // 提供者配置
        serviceConfig.setProvider(new ProviderConfig());
        // 应用配置
        serviceConfig.setApplication(new ApplicationConfig(HzkCommonConstants.APPLICATION_NAME_PROVIDER));
        // 注册配置
        serviceConfig.setRegistry(new RegistryConfig(HzkCommonConstants.REGISTRY_ZK_2181));
        // 服务导出
        serviceConfig.export();
        System.out.println("服务导出完成");
        // 阻塞主线程
        new CountDownLatch(1).await();
    }

}
