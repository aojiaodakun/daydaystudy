package com.hzk.consumer;

import com.hzk.callback.ICallbackListener;
import com.hzk.callback.ICallbackService;
import com.hzk.constants.HzkCommonConstants;
import com.hzk.factory.ConsumerFactory;
import com.hzk.service.IDemoService;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;

import java.util.concurrent.CountDownLatch;

/**
 26、参数回调
 */
public class ConsumerMain26 {

    public static void main(String[] args) throws Exception{
        ReferenceConfig<ICallbackService> referenceConfig = new ReferenceConfig<>();
        referenceConfig.setInterface(ICallbackService.class);
        // 应用配置
        referenceConfig.setApplication(new ApplicationConfig(HzkCommonConstants.APPLICATION_NAME_CONSUMER));
        // 注册配置
        referenceConfig.setRegistry(new RegistryConfig(HzkCommonConstants.REGISTRY_ZK_2181));

        // 服务引用
        ICallbackService callbackService = referenceConfig.get();
        System.out.println("服务引用完成");

        // 服务调用
        System.out.println("callback test start");
        callbackService.addListener("hzk", new ICallbackListener() {
            @Override
            public void changed(String msg) {
                System.out.println("changed " + msg);
            }
        });

        // 阻塞主线程
        new CountDownLatch(1).await();
    }

}
