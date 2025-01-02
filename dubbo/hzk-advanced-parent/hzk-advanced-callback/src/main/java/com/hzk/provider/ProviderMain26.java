package com.hzk.provider;

import com.hzk.callback.ICallbackListener;
import com.hzk.callback.ICallbackService;
import com.hzk.callback.impl.CallbackServiceImpl;
import com.hzk.factory.ProviderFactory;
import org.apache.dubbo.config.ArgumentConfig;
import org.apache.dubbo.config.MethodConfig;
import org.apache.dubbo.config.ServiceConfig;

import java.util.Arrays;
import java.util.concurrent.CountDownLatch;

/**
 26、参数回调
 */
public class ProviderMain26 {

    public static void main(String[] args) throws Exception{
        ServiceConfig serviceConfig = ProviderFactory.getCommonServiceConfig();
        serviceConfig.setInterface(ICallbackService.class);
        serviceConfig.setRef(new CallbackServiceImpl());
        /**
         * 26、参数回调
         */
        MethodConfig methodConfig = new MethodConfig();
        methodConfig.setName("addListener");
        ArgumentConfig argumentConfig = new ArgumentConfig();
        argumentConfig.setType(ICallbackListener.class.getName());
        argumentConfig.setIndex(1);
        argumentConfig.setCallback(true);
        methodConfig.setArguments(Arrays.asList(argumentConfig));

        serviceConfig.setMethods(Arrays.asList(methodConfig));

        // 服务导出
        serviceConfig.export();
        System.out.println("服务导出完成");
        // 阻塞主线程
        new CountDownLatch(1).await();
    }

}
