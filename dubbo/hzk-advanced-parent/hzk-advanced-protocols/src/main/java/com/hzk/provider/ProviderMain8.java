package com.hzk.provider;

import com.hzk.factory.ProviderFactory;
import org.apache.dubbo.config.ProtocolConfig;
import org.apache.dubbo.config.ServiceConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 8、多协议
 */
public class ProviderMain8 {

    public static void main(String[] args) throws Exception{
        ServiceConfig serviceConfig = ProviderFactory.getCommonServiceConfig();

        ProtocolConfig protocolConfig1 = serviceConfig.getProtocol();
        /**
         * 8、多协议
         */
        ProtocolConfig protocolConfig2 = new ProtocolConfig("http");
        protocolConfig2.setServer("tomcat");
        protocolConfig2.setPort(8001);

        List<ProtocolConfig> protocolConfigList = new ArrayList<>();
        protocolConfigList.add(protocolConfig1);
        protocolConfigList.add(protocolConfig2);
        serviceConfig.setProtocols(protocolConfigList);

        // 服务导出
        serviceConfig.export();
        System.out.println("服务导出完成");
        // 阻塞主线程
        new CountDownLatch(1).await();
    }

}
