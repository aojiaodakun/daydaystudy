package com.hzk.consumer;

import com.hzk.constants.HzkCommonConstants;
import com.hzk.factory.ConsumerFactory;
import com.hzk.service.IDemoService;
import com.hzk.service.IDemoService29;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;

/**
 29、本地伪装
 41、服务降级
 */
public class ConsumerMain29 {

    public static void main(String[] args) throws Exception{
        ReferenceConfig<IDemoService29> referenceConfig = new ReferenceConfig<>();
        referenceConfig.setInterface(IDemoService29.class);
        // 应用配置
        referenceConfig.setApplication(new ApplicationConfig(HzkCommonConstants.APPLICATION_NAME_CONSUMER));
        // 注册配置
        referenceConfig.setRegistry(new RegistryConfig(HzkCommonConstants.REGISTRY_ZK_2181));
        /**
         * 1、return：用于41、服务降级
         * 2、throw：用于41、服务降级
         * 3、force和fail
         *  3.1、forece：用于29、本地伪装
         *  3.2、fail：用于41、服务降级
         */
        mockReturnTest(referenceConfig);
//        mockThrowTest(referenceConfig);
//        mockForceTest(referenceConfig);

    }

    // 1、return。RpcException且非业务异常
    private static void mockReturnTest(ReferenceConfig<IDemoService29> referenceConfig){
        referenceConfig.setMock("return null");
        IDemoService29 demoService = referenceConfig.get();
        while (true) {
            System.out.println(demoService.mockReturn(""));
            System.out.println("-------------------------------");
            try {
                Thread.currentThread().sleep(1000*3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // 2、throw。调用异常，抛出指定异常类
    private static void mockThrowTest(ReferenceConfig<IDemoService29> referenceConfig){
        referenceConfig.setMock("throw com.hzk.exception.HzkException");
        IDemoService29 demoService = referenceConfig.get();
        while (true) {
            try {
                System.out.println(demoService.mockThrow(""));
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
            System.out.println("-------------------------------");
            try {
                Thread.currentThread().sleep(1000*3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 3、force。本地强制返回，不发起远程调用，测试用例使用
     * fail行为与默认的一致，仅为了区分force
     * @param referenceConfig
     */
    private static void mockForceTest(ReferenceConfig<IDemoService29> referenceConfig){
        referenceConfig.setMock("force:return fake");
        IDemoService29 demoService = referenceConfig.get();
        while (true) {
            System.out.println(demoService.mockForce(""));
            System.out.println("-------------------------------");
            try {
                Thread.currentThread().sleep(1000*3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
