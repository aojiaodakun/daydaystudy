package com.hzk.provider;


import com.hzk.service.DemoService;
import com.hzk.service.impl.DemoServiceImpl;
import com.hzk.service.impl.DemoServiceImpl2;
import org.apache.dubbo.config.*;

import java.util.ArrayList;
import java.util.List;

public class ProviderMain2 {

    static RegistryConfig staticRegistryConfig1;
    static RegistryConfig staticRegistryConfig2;
    static List<RegistryConfig> registryConfigList = new ArrayList<>();

    static{
        staticRegistryConfig1 = new RegistryConfig("zookeeper://127.0.0.1:2181");
        staticRegistryConfig1.setDefault(true);
        staticRegistryConfig2 = new RegistryConfig("zookeeper://127.0.0.1:2182");
        staticRegistryConfig1.setDefault(false);
        registryConfigList.add(staticRegistryConfig1);
        registryConfigList.add(staticRegistryConfig2);
    }

    public static void main(String[] args) throws Exception{
        ServiceConfig<DemoServiceImpl2> service = new ServiceConfig<>();
        service.setInterface(DemoService.class);
        service.setRef(new DemoServiceImpl2());
        service.setApplication(new ApplicationConfig("dubbo-demo-api-provider"));

        /**
         * 5、线程模型
         * 转发器：org.apache.dubbo.remoting.transport.netty4.NettyTransporter#bind
         * 线程池类型：org.apache.dubbo.common.threadpool.manager.DefaultExecutorRepository#getExecutor(org.apache.dubbo.common.URL)
         */
        ProtocolConfig protocolConfig = new ProtocolConfig("dubbo");
        protocolConfig.setPort(28889);
        // 只有请求响应消息派发到线程池，其它连接断开事件，心跳等消息，直接在 IO 线程上执行
        protocolConfig.setDispatcher("message");
        // 缓存线程池，空闲一分钟自动删除，需要时重建
        protocolConfig.setThreadpool("cached");
        service.setProtocol(protocolConfig);

        /**
         * 8、多协议
         */
//        ProtocolConfig protocolConfig2 = new ProtocolConfig("http");
//        protocolConfig2.setServer("tomcat");
//        protocolConfig2.setPort(8001);
//        List<ProtocolConfig> protocolConfigList = new ArrayList<>();
//        protocolConfigList.add(protocolConfig);
//        protocolConfigList.add(protocolConfig2);
//        service.setProtocols(protocolConfigList);
        /**
         * 10、服务分组
         */
        service.setGroup("hzk2");
        /**
         * 12、多版本
         */
        service.setVersion("1.0.0");

        RegistryConfig registryConfig = new RegistryConfig("zookeeper://127.0.0.1:2181");
        /**
         * 1、启动时检查。
         * 获取url参数时默认为true
         * 代码:org.apache.dubbo.registry.support.FailbackRegistry.register
          */
        registryConfig.setCheck(true);
        /**
         * 7、只订阅
         * 提供者不注册自己的信息到provider路径，影响消费者获取不到服务导致远程调用失败
         */
        registryConfig.setRegister(true);
        service.setRegistry(registryConfig);
        /**
         * 11、静态服务
         * 人工管理服务提供者的上线和下线
         * order:1、ProviderConfig；2、ServiceConfig；
         */
        // 11-1
//        ProviderConfig providerConfig = new ProviderConfig();
//        providerConfig.setDynamic(false);
//        service.setProvider(providerConfig);
//        // 11-2
//        service.setDynamic(false);




        /**
         * 3、集群容错
         * 注：提供者侧配置此参数无意义
         */
//        service.setCluster("failsafe");
        /**
         * 4、负载均衡
         */
        service.setLoadbalance("roundrobin");

        /**
         * 9、多注册中心
         */
//        service.setRegistries(registryConfigList);
        service.export();

        System.in.read();

    }


}
