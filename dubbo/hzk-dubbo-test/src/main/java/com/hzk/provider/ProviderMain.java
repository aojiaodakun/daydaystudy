package com.hzk.provider;


import com.hzk.callback.CallbackListener;
import com.hzk.service.DemoService;
import com.hzk.service.impl.DemoServiceImpl;
import org.apache.dubbo.config.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProviderMain {

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
        ServiceConfig<DemoServiceImpl> serviceConfig = new ServiceConfig<>();
        serviceConfig.setInterface(DemoService.class);

        DemoServiceImpl demoServiceImpl = new DemoServiceImpl();
        serviceConfig.setRef(demoServiceImpl);
        serviceConfig.setApplication(new ApplicationConfig("dubbo-demo-api-provider"));
        /**
         * 5、线程模型
         * 转发器：org.apache.dubbo.remoting.transport.netty4.NettyTransporter#bind
         * 线程池类型：org.apache.dubbo.common.threadpool.manager.DefaultExecutorRepository#getExecutor(org.apache.dubbo.common.URL)
         */
        ProtocolConfig protocolConfig = new ProtocolConfig("dubbo");
        protocolConfig.setPort(28888);
        // 只有请求响应消息派发到线程池，其它连接断开事件，心跳等消息，直接在 IO 线程上执行
        protocolConfig.setDispatcher("message");
        // 缓存线程池，空闲一分钟自动删除，需要时重建
        protocolConfig.setThreadpool("cached");
        /**
         * 48、访问日志
         */
//        protocolConfig.setAccesslog("E://test//dubbo//access.log");

        /**
         * 32、连接控制
         */
//        protocolConfig.setAccepts(1);

        /**
         * 44、主机绑定
         */
//        protocolConfig.setHost("192.168.44");
        /**
         * 45、主机配置
         */
//        System.setProperty("DUBBO_IP_TO_REGISTRY", "192.168.44.1");
//        System.setProperty("DUBBO_PORT_TO_REGISTRY", "9000");
//        System.setProperty("DUBBO_IP_TO_BIND", "192.168.44.2");
//        System.setProperty("DUBBO_PORT_TO_BIND", "9001");




        serviceConfig.setProtocol(protocolConfig);
        /**
         * 8、多协议
         */
//        ProtocolConfig protocolConfig2 = new ProtocolConfig("http");
//        protocolConfig2.setServer("tomcat");
//        protocolConfig2.setPort(8001);
//        List<ProtocolConfig> protocolConfigList = new ArrayList<>();
//        protocolConfigList.add(protocolConfig);
//        protocolConfigList.add(protocolConfig2);
//        serviceConfig.setProtocols(protocolConfigList);
        /**
         * 10、服务分组
         */
        serviceConfig.setGroup("hzk1");
        /**
         * 12、多版本
         */
        serviceConfig.setVersion("1.0.0");
        /**
         * 14、参数验证测试
         */
        serviceConfig.setValidation("true");

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

        /**
         * 46、注册信息简化
         */
//        registryConfig.setSimplified(true);
        serviceConfig.setTimeout(1000 * 20);
        registryConfig.setExtraKeys("delay");
        serviceConfig.setRegistry(registryConfig);


        /**
         * 11、静态服务
         * 人工管理服务提供者的上线和下线
         * order:1、ProviderConfig；2、ServiceConfig；
         */
        // 11-1
//        ProviderConfig providerConfig = new ProviderConfig();
//        providerConfig.setDynamic(false);
//        serviceConfig.setProvider(providerConfig);
//        // 11-2
//        serviceConfig.setDynamic(false);




        /**
         * 3、集群容错
         * 注：提供者侧配置此参数无意义
         */
//        serviceConfig.setCluster("failsafe");
        /**
         * 4、负载均衡
         */
        serviceConfig.setLoadbalance("roundrobin");

        /**
         * 9、多注册中心
         */
//        serviceConfig.setRegistries(registryConfigList);

        /**
         * 26、参数回调
         */
        MethodConfig methodConfig = new MethodConfig();
        methodConfig.setName("addListener");
        ArgumentConfig argumentConfig = new ArgumentConfig();
        argumentConfig.setType(CallbackListener.class.getName());
        argumentConfig.setIndex(1);
        argumentConfig.setCallback(true);
        methodConfig.setArguments(Arrays.asList(argumentConfig));

        serviceConfig.setMethods(Arrays.asList(methodConfig));

        /**
         * 30、延迟暴露
         */
//        serviceConfig.setDelay(1000 * 5);
        /**
         * 31、并发控制
         */
//        serviceConfig.setExecutes(1);
//        serviceConfig.setActives(1);

        /**
         * 36、令牌验证
         */
        serviceConfig.setToken(true);
        /**
         * 37、路由规则
         */
//        serviceConfig.setTag("tag1");

        // 服务导出
        serviceConfig.export();


        /**
         * 25、本地调用
         */
//        injvmTest();

        System.in.read();

    }


    private static void injvmTest(){
        ReferenceConfig<DemoService> referenceConfig = new ReferenceConfig<>();
        referenceConfig.setInterface(DemoService.class);
        // 仅远程调用
        referenceConfig.setScope("injvm");
        referenceConfig.setApplication(new ApplicationConfig("dubbo-demo-api-consumer"));
        referenceConfig.setTimeout(1000 * 20);
        RegistryConfig registryConfig = new RegistryConfig("zookeeper://127.0.0.1:2181");
        referenceConfig.setRegistry(registryConfig);
        referenceConfig.setGroup("hzk1");
        /**
         * 12、多版本
         */
        referenceConfig.setVersion("1.0.0");
        DemoService demoService = referenceConfig.get();
        String result = demoService.sayHello("hzk");
        System.out.println(result);

    }


}
