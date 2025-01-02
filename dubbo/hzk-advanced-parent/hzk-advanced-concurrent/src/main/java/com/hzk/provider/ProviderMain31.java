package com.hzk.provider;

import com.hzk.constants.ConcurrentConstants;
import com.hzk.factory.ProviderFactory;
import com.hzk.service.IDemoServiceImpl31;
import org.apache.dubbo.config.ServiceConfig;

import java.util.concurrent.CountDownLatch;

/**
 31、并发控制
 */
public class ProviderMain31 {

    public static void main(String[] args) throws Exception{
        ServiceConfig serviceConfig = ProviderFactory.getCommonServiceConfig();
        serviceConfig.setRef(new IDemoServiceImpl31());

        // 测试时，切换com.hzk.constants.ConcurrentConstants.CONCURRENT_CONTROL
        if (ConcurrentConstants.CONCURRENT_CONTROL.equals(ConcurrentConstants.CONCURRENT_CONTROL_PROVIDER) ||
                ConcurrentConstants.CONCURRENT_CONTROL.equals(ConcurrentConstants.CONCURRENT_CONTROL_ALL)) {
            // 提供者并发控制
            serviceConfig.setExecutes(1);
        }
        // 服务导出
        serviceConfig.export();
        System.out.println("服务导出完成");
        // 阻塞主线程
        new CountDownLatch(1).await();
    }

}
