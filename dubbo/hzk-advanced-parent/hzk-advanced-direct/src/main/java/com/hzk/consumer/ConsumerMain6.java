package com.hzk.consumer;

import com.hzk.factory.ConsumerFactory;
import com.hzk.service.IDemoService;
import org.apache.dubbo.config.ReferenceConfig;

import static com.hzk.constants.HzkCommonConstants.PROTOCOL_DUBBO_DEFAULT_PORT;

/**
 6、直连提供者
 */
public class ConsumerMain6 {

    public static void main(String[] args) throws Exception{
        ReferenceConfig<IDemoService> referenceConfig = ConsumerFactory.getCommonReferenceConfig();
        /**
         * 6、直连提供者
         * 注：不经过注册中心。
         * 格式：提供者的通信协议，ip和端口
         * dubbo协议默认端口为20880，org.apache.dubbo.rpc.protocol.dubbo.DubboProtocol#DEFAULT_PORT
         */
        referenceConfig.setUrl("dubbo://127.0.0.1:" + PROTOCOL_DUBBO_DEFAULT_PORT);
        // 服务引用
        IDemoService demoService = referenceConfig.get();
        System.out.println("服务引用完成");
        // 服务调用
        while (true) {
            String name = "hzk";
            System.out.println("remote invoke,param:" + name);
            demoService.sayHello(name);
            Thread.currentThread().sleep(1000*3);
        }

    }

}
