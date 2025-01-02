package com.hzk.provider;

import com.hzk.constants.ConnectConstants;
import com.hzk.factory.ProviderFactory;
import com.hzk.service.IDemoServiceImpl32;
import org.apache.dubbo.config.ServiceConfig;

import java.util.concurrent.CountDownLatch;

/**
 32、连接控制
 */
public class ProviderMain32 {

    public static void main(String[] args) throws Exception{
        ServiceConfig serviceConfig = ProviderFactory.getCommonServiceConfig();
        serviceConfig.setRef(new IDemoServiceImpl32());

        // 测试时，切换com.hzk.constants.ConnectConstants.CONNECT_CONTROL
        if (ConnectConstants.CONNECT_CONTROL.equals(ConnectConstants.CONNECT_CONTROL_PROVIDER) ||
                ConnectConstants.CONNECT_CONTROL.equals(ConnectConstants.CONNECT_CONTROL_ALL)) {
            // 连接控制
            serviceConfig.getProtocol().setAccepts(1);
        }
        // 服务导出
        serviceConfig.export();
        System.out.println("服务导出完成");
        // 阻塞主线程
        new CountDownLatch(1).await();
    }

}
