package com.hzk.framework.lifecycle;

import com.hzk.dubbo.factory.ProviderFactory;
import org.apache.dubbo.config.ServiceConfig;

import java.util.concurrent.CountDownLatch;

public class DubboService implements Service {

    private boolean isStarted;

    @Override
    public String getName() {
        return DubboService.class.getName();
    }

    @Override
    public void start() throws InterruptedException {
        String appName = System.getProperty("appName");
        if (appName.equals("web")) {
            return;
        }

        ServiceConfig serviceConfig = ProviderFactory.getCommonServiceConfig();
        serviceConfig.getRegistry().setSimplified(true);
        /**
         * 10、服务分组
         */
//        serviceConfig.setGroup("bos");


        serviceConfig.getProtocol().setPort(Integer.getInteger("dubbo.protocol.port"));


        /**
         * 服务导出
         * 1、拼接参数ip+port生成URL对象
         * 2、本地导出，起netty监听20880端口
         * 3、远程导出，将URL参数，包含ip，port，接口等信心注册到注册中心（zk）
         */
        serviceConfig.export();
        System.out.println("DubboService服务导出完成");
        System.out.println("DubboService结束");
        isStarted = true;

        // 阻塞主线程
//        new CountDownLatch(1).await();
    }
    @Override
    public void stop() {

    }

    @Override
    public boolean isStarted() {
        return isStarted;
    }
}
