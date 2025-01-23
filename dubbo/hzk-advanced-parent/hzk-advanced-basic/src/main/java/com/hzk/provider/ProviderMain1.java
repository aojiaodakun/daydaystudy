package com.hzk.provider;

import com.hzk.constants.HzkCommonConstants;
import com.hzk.factory.ProviderFactory;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.dubbo.config.ServiceConfig;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;

import java.util.concurrent.CountDownLatch;

/**
 * 1、启动时检查
 * 7、只订阅
 * 30、延迟暴露
 * 33、延迟连接
 * 46、注册信息简化
 */
public class ProviderMain1 {

    public static void main(String[] args) throws Exception{
        ServiceConfig serviceConfig = ProviderFactory.getCommonServiceConfig();
//        /**
//         * 1、启动时检查
//         */
//        serviceConfig.getRegistry().setCheck(Boolean.TRUE);
//        /**
//         * 7、只订阅
//         */
//        serviceConfig.getRegistry().setRegister(Boolean.TRUE);
//        /**
//         * 30、延迟暴露
//         */
//        serviceConfig.setDelay(1000 * 5);
//        /**
//         * 46、注册信息简化
//         */
        serviceConfig.getRegistry().setSimplified(true);
        /**
         * 10、服务分组
         */
        serviceConfig.setGroup("bos");

        serviceConfig.getProtocol().setPort(20881);


        /**
         * 服务导出
         * 1、拼接参数ip+port生成URL对象
         * 2、本地导出，起netty监听20880端口
         * 3、远程导出，将URL参数，包含ip，port，接口等信心注册到注册中心（zk）
         */
        serviceConfig.export();
        System.out.println("服务导出完成");

        //-------------------------------------------------
        // zk注册monitor节点
//        CuratorFramework client;
//        String url = "localhost:2181";
//        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000,3);
//        client = CuratorFrameworkFactory.builder()
//                .connectString(url)
//                .sessionTimeoutMs(1000 * 2000)
//                .retryPolicy(retryPolicy)
//                .build();
//        client.start();
//        byte[] bytes = HzkCommonConstants.APPLICATION_NAME_PROVIDER.getBytes();
//        client.create()
//                .withMode(CreateMode.PERSISTENT)
//                .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
//                .forPath("/monitor/runtime/nodes1", bytes);
        System.out.println("结束");

        // 阻塞主线程
        new CountDownLatch(1).await();
    }

}
