package com.hzk.consumer;

import com.hzk.constants.HzkCommonConstants;
import com.hzk.factory.ConsumerFactory;
import com.hzk.service.IDemoService;
import com.hzk.service.IDemoService21;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.rpc.RpcContext;

import java.util.UUID;

/**
 21、上下文信息
 22、隐式参数
 */
public class ConsumerMain21 {

    public static void main(String[] args) throws Exception{
        ReferenceConfig<IDemoService21> referenceConfig = new ReferenceConfig<>();
        referenceConfig.setInterface(IDemoService21.class);
        // 应用配置
        referenceConfig.setApplication(new ApplicationConfig(HzkCommonConstants.APPLICATION_NAME_CONSUMER));
        // 注册配置
        referenceConfig.setRegistry(new RegistryConfig(HzkCommonConstants.REGISTRY_ZK_2181));
        // 服务引用
        IDemoService21 demoService = referenceConfig.get();
        System.out.println("服务引用完成");
        // 服务调用
        while (true) {
            String uuid = UUID.randomUUID().toString();
            System.out.println("remote invoke,param:" + uuid);
            RpcContext.getContext().setAttachment("divParam", uuid);
            demoService.rpcContextTest("hzk");
            Thread.currentThread().sleep(1000*3);
        }

    }

}
