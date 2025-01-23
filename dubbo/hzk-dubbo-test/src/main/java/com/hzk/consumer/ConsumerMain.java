package com.hzk.consumer;


import com.hzk.callback.CallbackListener;
import com.hzk.notify.Notify;
import com.hzk.notify.NotifyImpl;
import com.hzk.service.DemoService;
import com.hzk.stub.DemoServiceStub;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.MethodConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.utils.ReferenceConfigCache;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class ConsumerMain {

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

    public static void main(String[] args) throws Exception {
        ReferenceConfig<DemoService> referenceConfig = new ReferenceConfig<>();
        referenceConfig.setInterface(DemoService.class);
        // 仅远程调用
        referenceConfig.setScope("remote");
        referenceConfig.setApplication(new ApplicationConfig("dubbo-demo-api-consumer"));
        referenceConfig.setTimeout(1000 * 20);
        RegistryConfig registryConfig = new RegistryConfig("zookeeper://127.0.0.1:2181");
        /**
         * 1、启动时检查。
         * 值为空时默认为true
         * 代码:org.apache.dubbo.config.ReferenceConfig#checkInvokerAvailable()
         */
        registryConfig.setCheck(true);
        /**
         * 7、只订阅
         * 消费者不注册自己的信息到consumer路径
         */
        registryConfig.setRegister(true);
        referenceConfig.setRegistry(registryConfig);
        /**
         * 3、集群容错
         * 仅读取消费者侧配置
         * org.apache.dubbo.registry.integration.RegistryProtocol#refer(java.lang.Class, org.apache.dubbo.common.URL)
         * Cluster cluster = Cluster.getCluster(qs.get(CLUSTER_KEY));
         */
        referenceConfig.setCluster("failsafe");

        /**
         * 4、负载均衡
         * 注：启动时以消费侧为准，运行时以配置中心为准
         * 获取到provider解析时，本地url覆盖远程url
         * org.apache.dubbo.registry.integration.RegistryDirectory#toInvokers(java.util.List)
         *      URL url = mergeUrl(providerUrl);
         * 使用时
         * org.apache.dubbo.rpc.cluster.support.AbstractClusterInvoker#invoke(org.apache.dubbo.rpc.Invocation)
         */
        referenceConfig.setLoadbalance("random");
        /**
         * 6、直连提供者
         * 注：不经过注册中心
         */
//        referenceConfig.setUrl("dubbo://127.0.0.1:20880");
        /**
         * 9、多注册中心
         */
//        referenceConfig.setRegistries(registryConfigList);
        /**
         * 10、服务分组
         */
        referenceConfig.setGroup("hzk1");
        referenceConfig.setGroup("*");
//        referenceConfig.setGroup("hzk1,hzk2");
//        referenceConfig.setMerger("true");
        /**
         * 12、多版本
         */
        referenceConfig.setVersion("1.0.0");
        /**
         * 14、参数验证测试
         */
        referenceConfig.setValidation("true");
        /**
         * 15、结果缓存
         */
//        referenceConfig.setCache("lru");
        /**
         * 15、泛化调用
         */
//        referenceConfig.setGeneric(true);
        /**
         * 27、事件通知
         */
//        MethodConfig methodConfig = new MethodConfig();
//        methodConfig.setName("sayHello");
//        methodConfig.setAsync(false);
//        Notify notify = new NotifyImpl();
//        // 调用前
//        methodConfig.setOninvoke(notify);
//        methodConfig.setOninvokeMethod("oninvoke");
//        // 调用后
//        methodConfig.setOnreturn(notify);
//        methodConfig.setOnreturnMethod("onreturn");
//        // 报异常
//        methodConfig.setOnthrow(notify);
//        methodConfig.setOnthrowMethod("onthrow");
//        referenceConfig.setMethods(Arrays.asList(methodConfig));

        /**
         * 28、本地存根测试
         */
//        referenceConfig.setStub(DemoServiceStub.class.getName());
        /**
         * 29、本地伪装测试
         * 1、return
         * 2、throw
         * 3、force和fail
         */
//        // 1、return。RPCException且非业务异常
//        referenceConfig.setMock("return null");
//        // 2、throw。调用异常，抛出指定异常类
//        referenceConfig.setMock("throw com.hzk.exception.HzkException");
//        /**
//         * 3、force。本地强制返回，测试用例使用
//         * fail行为与默认的一致，仅为了区分force
//         */
//        referenceConfig.setMock("force:return fake");

        /**
         * 31、并发控制
         */
        referenceConfig.setLoadbalance("leastactive");
        /**
         * 32、连接控制
         */
//        referenceConfig.setConnections(1);
        /**
         * 33、延迟连接
         */
        referenceConfig.setLazy(true);
        /**
         * 34、粘滞连接
         */
//        referenceConfig.setSticky(true);
        /**
         * 50、ReferenceConfig缓存
         */
        ReferenceConfigCache referenceConfigCache = ReferenceConfigCache.getCache();
        DemoService demoService = referenceConfigCache.get(referenceConfig);

        // 服务引用
//        DemoService demoService = referenceConfig.get();
//        String message = demoService.sayHello("hzk");
//        System.out.println(message);

        /**
         * 31、并发控制
         */
//        new Thread(()->{
//            // exception
//            String message = demoService.sayHello("sleep");
//            System.out.println(message);
//        }).start();
//        Thread.currentThread().sleep(1000*1);
//        System.out.println("concurrent start");
//        for (int i = 0; i < 5; i++) {
//            new Thread(()->{
//                long startTime = System.currentTimeMillis();
//                String tempResult = demoService.sayHello("sleep");
//                long endTime = System.currentTimeMillis();
//                long betweenTime = endTime - startTime;
//                System.out.println("thread:" + Thread.currentThread().getName() + ",result:" + tempResult + ",time:" + betweenTime);
//            },"hzk-" + i).start();
//        }


        /**
         * 15、泛化调用
         * org.apache.dubbo.rpc.service.GenericService可以替代所有接口引用
         */
//        GenericService genericService = (GenericService)demoService;
//        Object result = genericService.$invoke("sayHello", new String[]{String.class.getName()}, new Object[]{"hzk"});
//        System.out.println(result);

        /**
         * 20、回声测试
         *
         */
//        EchoService echoService = (EchoService)demoService;
//        Object result = echoService.$echo("OK");
//        System.out.println(result);
        /**
         * 24、异步调用
         */
        // 异步方式1：调用直接返回CompletableFuture
//        CompletableFuture<String> future = demoService.asyncCall("async call request");
//        // 增加回调
//        future.whenComplete((v, t) -> {
//            if (t != null) {
//                t.printStackTrace();
//            } else {
//                System.out.println("Response: " + v);
//            }
//        });
//        // 早于结果输出
//        System.out.println("Executed before response return.");

        // 异步方式2
//        demoService.asyncCall2("async");// 返回null
//        CompletableFuture<String> helloFuture = RpcContext.getContext().getCompletableFuture();
//        // 为Future添加回调
//        helloFuture.whenComplete((retValue, exception) -> {
//            if (exception == null) {
//                System.out.println(retValue);
//            } else {
//                exception.printStackTrace();
//            }
//        });

        // 异步方式3
//        CompletableFuture<String> future3 = RpcContext.getContext().asyncCall(()->{
//            return demoService.sayHello("async call request");
//        });
//        future3.get();

//        /**
//         * 26、参数回调
//         */
//        System.out.println("callback test start");
//        demoService.addListener("hzk", new CallbackListener() {
//            @Override
//            public void changed(String msg) {
//                System.out.println("changed " + msg);
//            }
//        });

        new Thread(()->{
            // 同步调用
            while (true) {
                try {
                    Thread.sleep(1000 * 3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String message = demoService.sayHello("dubbo");
                System.out.println(message);
            }
        }).start();



        /**
         * 13、分组聚合测试
         */
//        List<String> resultList = demoService.merge("dubbo");
//        System.out.println(resultList);

        /**
         * 14、参数验证测试
         */
//        ExampleParamDTO paramDTO = new ExampleParamDTO();
//        paramDTO.setName("hzk");
//        paramDTO.setAge(17);
//        String result = demoService.paramValid(paramDTO);
//        System.out.println(result);


        // generic invoke
//        GenericService genericService = (GenericService) demoService;
//        Object genericInvokeResult = genericService.$invoke("sayHello", new String[] { String.class.getName() },
//                new Object[] { "dubbo generic invoke" });
//        System.out.println(genericInvokeResult);


          new CountDownLatch(1).await();
    }

}
