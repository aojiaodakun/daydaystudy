## 官网链接：https://dubbo.apache.org/zh/docs/advanced/
## 官网demo示例：https://github.com/apache/dubbo-samples，基于springIOC
#### module详情

| module | 模块特性 | 备注 |
| :----| :---- | :---- |
| <font color=red>hzk-advanced-parent</font> | 高级用法父module | 无demo项<br>2、序列化协议安全<br>5、线程模型<br>35、TLS<br>39、配置规则<br>40、旧配置规则<br>42、消费端线程池模型<br>43、优雅停机<br>47、日志适配<br>49、服务容器 |
| hzk-advanced-basic | 1、启动时检查<br>7、只订阅<br>30、延迟暴露<br>33、延迟连接<br>46、注册信息简化 | - |
| hzk-advanced-fault-tolerant | 3、集群容错 | - |
| hzk-advanced-loadbalance | 4、负载均衡 | - |
| hzk-advanced-direct | 6、直连提供者 | - |
| hzk-advanced-protocols | 8、多协议 | - |
| hzk-advanced-registries | 9、多注册中心 | - |
| hzk-advanced-group | 10、服务分组 | - |
| hzk-advanced-not-dynamic | 11、静态服务 | - |
| hzk-advanced-versions | 12、多版本 | - |
| hzk-advanced-merger | 13、分组聚合 | - |
| hzk-advanced-validation | 14、参数验证 | - |
| hzk-advanced-cache-result | 15、结果缓存 | - |
| hzk-advanced-generic | 16、使用泛化调用<br>19、实现泛化调用 | - |
| hzk-advanced-protobuf | 17、Protobuf | - |
| hzk-advanced-protobuf-generic | 18、GoogleProtobuf 对象泛化调用 | - |
| hzk-advanced-echo | 20、回声测试 | - |
| hzk-advanced-context | 21、上下文信息<br>22、隐式参数 | - |
| hzk-advanced-async | 23、异步执行<br>24、异步调用 | - |
| hzk-advanced-injvm | 25、本地调用 | - |
| hzk-advanced-callback | 26、参数回调 | - |
| hzk-advanced-event | 27、事件通知 | - |
| hzk-advanced-stub | 28、本地存根 | - |
| hzk-advanced-mock | 29、本地伪装<br>41、服务降级 | - |
| hzk-advanced-concurrent | 31、并发控制 | - |
| hzk-advanced-connect | 32、连接控制 | - |
| hzk-advanced-sticky | 34、粘滞连接 | - |
| hzk-advanced-token | 36、令牌验证 | - |
| hzk-advanced-route | 37、路由规则<br>38、旧路由规则 | - |
| hzk-advanced-config | 39、配置规则<br>40、旧配置规则 | - |
| hzk-advanced-host | 44、主机绑定<br>45、主机配置 | - |
| hzk-advanced-accesslog | 48、访问日志 | - |
| hzk-advanced-cache-referenceconfig | 50、ReferenceConfig缓存 | - |


#### 1、启动时检查
> 提供者

> 备注
* registryConfig.setCheck(true);
* 获取url参数时默认为true
* 代码:org.apache.dubbo.registry.support.FailbackRegistry.register
> 消费者
* 值为空时默认为true
* 代码:org.apache.dubbo.config.ReferenceConfig#checkInvokerAvailable()

#### 2、序列化协议安全
> 备注
* dubbo3.0序列化机制
* dubbo2.0，token机制+注册中心鉴权

#### 3、集群容错
> 提供者
* service.setCluster("failsafe")，配置无意义
> 消费者
* org.apache.dubbo.registry.integration.RegistryProtocol#refer(java.lang.Class, org.apache.dubbo.common.URL)
* Cluster cluster = Cluster.getCluster(qs.get(CLUSTER_KEY));

#### 4、负载均衡
> 备注
* 启动时以消费侧为准，运行时以配置中心为准
> 提供者
* service.setLoadbalance("roundrobin");
> 消费者
* reference.setLoadbalance("roundrobin");
* 获取到provider解析时，本地url覆盖远程url
* org.apache.dubbo.registry.integration.RegistryDirectory#toInvokers(java.util.List)
* URL url = mergeUrl(providerUrl);
* 调用时实例化
* org.apache.dubbo.rpc.cluster.support.AbstractClusterInvoker#invoke(org.apache.dubbo.rpc.Invocation)

#### 5、线程模型
> 备注
* 转发器仅在接收消息使用
* org.apache.dubbo.remoting.transport.netty4.NettyTransporter#bind
线程池类型
* 一个容器内同时存在side=provider或side=consumer的2个线程池
* org.apache.dubbo.common.threadpool.manager.DefaultExecutorRepository#getExecutor(org.apache.dubbo.common.URL)

#### 6、直连提供者
> 备注
* 不经过注册中心，仅本地调试使用
> 消费者
* reference.setUrl("dubbo://127.0.0.1:20880");
* org.apache.dubbo.config.ReferenceConfig.createProxy中url!=null的逻辑

#### 7、只订阅
* registryConfig.setRegister(true);
> 提供者
* 提供者不注册自己的信息到provider路径，影响消费者获取不到服务导致远程调用失败
> 消费者
* 消费者不注册自己的信息到consumer路径，不影响调用，建议开启

#### 8、多协议
> 备注
* 适用场景：不同服务在性能上适用不同协议进行传输，比如大数据用短连接协议，小数据大并发用长连接协议
> 提供者
* ProtocolConfig protocolConfig2 = new ProtocolConfig("http");
* protocolConfig2.setServer("tomcat");
* protocolConfig2.setPort(8001);
* serviceConfig.setProtocols(protocolConfigList);
> 消费者
* 集群调用时通过负载均衡算法选择RegistryDirectory#urlInvokerMap，M->N 
* org.apache.dubbo.rpc.cluster.support.AbstractClusterInvoker.doSelect

#### 9、多注册中心
> 备注
* 消费者的RegistryConfig的isDefault和preferred，仅在某个特定阶段版本起效，在当前dubbox-2.8.4无效
> 提供者
* serviceConfig.setRegistries(registryConfigList);
> 消费者

| 产商 | 版本 | 类 | 关联属性 |
| :----:| :----: | :----: | :----: |
| apache | 2.7.5前 | RegistryAwareClusterInvoker | isDefault |
| apache | 2.7.8 | ZoneAwareClusterInvoker | preferred |
| dubbox | 2.8.4 | AvailableCluster$1 | 无 |

#### 10、服务分组,group
> 备注
* 苍穹使用group来区分appId
* org.apache.dubbo.common.utils.UrlUtils.isMatch，消费者url与提供者url匹配

#### 11、静态服务
> 备注,慎用
* 人工管理服务提供者的上线和下线
* 服务提供者初次注册时为禁用状态，需人工启用。断线时，将不会被自动删除，需人工禁用，结合dubbo-admin使用
> 提供者
* providerConfig.setDynamic(false);
* org.apache.dubbo.registry.zookeeper.ZookeeperRegistry.doRegister。zk节点为永久节点

#### 12、多版本,version
> 备注
* 苍穹灰度遮掩ServiceConfig，新增appGroup属性，URL匹配时遮掩org.apache.dubbo.common.utils.UrlUtils.isMatch
> 提供者
* serviceConfig.setVersion("1.0.0");
> 消费者
* referenceConfig.setVersion("1.0.0");

#### 13、分组聚合
> 备注
* 消费者并发请求n个group，hzk1或hzk2请求顺序不定，然后获取响应值后合并。org.apache.dubbo.rpc.cluster.support.MergeableClusterInvoker.doInvoke
* reference.setGroup("hzk1,hzk2");
* reference.setMerger("true");// 不是true，则顺序选择hzk1,hzk2，内存不计数，非轮询

#### 14、参数验证
> 备注
* 参数验证功能是基于 JSR303 实现的，用户只需标识 JSR303 标准的验证 annotation，并通过声明 filter 来实现验证
* 提供者侧或消费者侧构建FilterChain时，多创建ValidationFilter
> 提供者
* serviceConfig.setValidation("true");
> 消费者
* referenceConfig.setValidation("true");

#### 15、结果缓存
> 备注
* org.apache.dubbo.cache.filter.CacheFilter
> 消费者
* referenceConfig.setCache("lru");

#### 16、使用泛化调用
> 备注
* 苍穹的微服务接口DispatchService未使用泛化，消费者侧显示指定方法名methodName,paramTypes,args，提供者侧自己实现通过反射进行调用
> 提供者
* org.apache.dubbo.rpc.filter.GenericFilter
> 消费者
* org.apache.dubbo.rpc.filter.GenericImplFilter
* 消费者侧获取RpvInvocation的methodName,paramTypes,args，重新实例化一个RpvInvocation

#### 17、Protobuf
> 备注，TODO
* maven插件解析Protobuf文件产生java文件，ServiceMesh的延伸

#### 18、GoogleProtobuf 对象泛化调用
> 备注，TODO

#### 19、实现泛化调用
> 备注，参考16、使用泛化调用

#### 20、回声测试，即ping-pong
> 备注
* 苍穹的回声测试：HeaderExchangeChannel#handShakeBeforeRequest()。
* 苍穹的上下文RequestContextFilter注入到Invocation，数据包较大；故不适合使用原生dubbo的EchoService
> 提供者
* org.apache.dubbo.rpc.filter.EchoFilter,返回值为请求的第一个入参
> 消费者
* EchoService echoService = (EchoService)demoService;
* Object result = echoService.$echo("OK");

#### 21、上下文信息
> 备注
* 每次远程调用会实例化一个ThreadLocal对象存储上下文信息，获取方式：RpcContext.getContext()

#### 22、隐式参数
> 提供者
* RpcContext.getContext().getAttachment("index")
> 消费者
* RpcContext.getContext().setAttachment("index", "1")

#### 23、异步执行
> 提供者
* 接口返回值为CompletableFuture<T>

#### 24、异步调用
> 消费者
```java
CompletableFuture<String> future = demoService.asyncCall("async call request");
// 增加回调
future.whenComplete((v, t) -> {
    if (t != null) {
        t.printStackTrace();
    } else {
        System.out.println("Response: " + v);
    }
});
```

#### 25、本地调用
> 备注
* 此方式是同条线程内调用
* 苍穹本地调试为dubbo直连，即System.setProperty("dubbo.consumer.url", "dubbo://localhost:20880");参考章节6
> 消费者
* referenceConfig.setScope("injvm");或protocolConfig.setName("injvm");

#### 26、参数回调
> 备注
* Consumer发起远程调用前，开启netty监听。提供者获得监听代理类，随时可以向Consumer发起远程调用
* 无cluster,filter，仅invoker。苍穹无法传递上下文，暂不支持
> 提供者
* 实参解码(反序列化)中，构建一个新的代理对象，无cluster,filter，仅invoker
* org.apache.dubbo.rpc.protocol.dubbo.CallbackServiceCodec.referOrDestroyCallbackService
> 消费者
* 实参编码(序列化)中，启动netty监听，无cluster,filter，仅invoker
* org.apache.dubbo.rpc.protocol.dubbo.CallbackServiceCodec.exportOrUnexportCallbackService

#### 27、事件通知(AOP)
org.apache.dubbo.rpc.protocol.dubbo.filter.FutureFilter.fireInvokeCallback
> 备注
* 在调用之前、调用之后、出现异常时的事件通知
* org.apache.dubbo.rpc.protocol.dubbo.filter.FutureFilter

#### 28、本地存根
> 备注
* 消费者侧构造一个默认的接口实现，封装proxy，发起远程调用前进行参数预校验。
> 消费者
* org.apache.dubbo.rpc.proxy.wrapper.StubProxyFactoryWrapper.getProxy(org.apache.dubbo.rpc.Invoker<T>, boolean)

#### 29、本地伪装
> 备注
* 通常用于服务降级，比如某验权服务，当服务提供方全部挂掉后，客户端不抛出异常，而是通过 Mock 数据返回授权失败
* 1、return。RPCException且非业务异常
* 2、throw。调用异常，抛出指定异常类
* 3、force。本地强制返回，测试用例使用。fail行为与默认的一致，仅为了区分force
* org.apache.dubbo.rpc.cluster.support.wrapper.MockClusterInvoker.invoke
> 消费者
* referenceConfig.setMock("force:return fake");

#### 30、延迟暴露
> 消费者
* serviceConfig.setDelay(1000 * 5);// 延迟5秒进行服务导出
* org.apache.dubbo.config.ServiceConfigBase.shouldDelay

#### 31、并发控制
> executes变量

> 备注
* 服务器端并发执行（或占用线程池线程数）不能超过 n 个
> 提供者
* serviceConfig.setExecutes(1);
* org.apache.dubbo.rpc.filter.ExecuteLimitFilter，并发Map当计数器
* dubbo框架异常，org.apache.dubbo.remoting.exchange.support.header.HeaderExchangeHandler.handleRequest
> 消费者
* 并发上限异常的状态码为70，消费者侧不接收result
* org.apache.dubbo.rpc.protocol.dubbo.DubboCodec.decodeBody

> actives变量

> 备注
* 每客户端并发执行（或占用连接的请求数）不能超过 n 个
* 提供者或消费者都可以设置，以消费者侧为准
> 消费者
* org.apache.dubbo.rpc.filter.ActiveLimitFilter

> 温馨提示
* 官方建议：并发控制与loadbalance=leastactive配置使用。
* ActiveLimitFilter往RpcStatus的SERVICE_STATISTICS写某个接口的方法的调用次数，LeastActiveLoadBalance读取

#### 32、连接控制
> accepts，服务端配置项

> 提供者
* protocolConfig.setAccepts(1);
* org.apache.dubbo.remoting.transport.AbstractServer.connected
> 消费者
* org.apache.dubbo.remoting.transport.netty4.NettyClientHandler.channelInactive

> connections，消费者配置项
* org.apache.dubbo.rpc.protocol.dubbo.DubboProtocol.getClients

#### 33、延迟连接
> 消费者
* referenceConfig.setLazy(true);
* org.apache.dubbo.rpc.protocol.dubbo.DubboProtocol.initClient

#### 34、粘滞连接
> 备注
* 为有状态服务配置粘滞连接。微服务接口一般为无状态服务，不使用
> 消费者
* referenceConfig.setSticky(true);
* org.apache.dubbo.rpc.cluster.support.AbstractClusterInvoker.select

#### 35、TLS
> 备注
* 客户端服务端互信，证书认证，不做测试

#### 36、令牌验证
> 备注
* 防止消费者越过注册中心，如dubbo直连消费者
> 提供者
* serviceConfig.setToken(true);
* org.apache.dubbo.rpc.filter.TokenFilter

#### 37、路由规则
> 备注
* 苍穹的灰度参数AppGroup属于遮掩参数，建议修改成标签路由，可通过Dubbo-Admin控制台管理。不至于导致服务不可用且无法挽回
> 条件路由，暂不测试

> 标签路由

> 提供者
* serviceConfig.setTag("tag1");
> 消费者
* org.apache.dubbo.rpc.cluster.router.tag.TagRouter.route

#### 38、旧路由规则，略

#### 39、配置规则
> 备注
* 支持应用级治理和服务级治理，需通过Dubbo-Admin控制台实时修改
* org.apache.dubbo.registry.integration.RegistryDirectory.notify

#### 40、旧配置规则，略

#### 41、服务降级，参考章节29、本地伪装

#### 42、消费端线程池模型，略

#### 43、优雅停机
> 备注
* 缺省超时时间是 10 秒，dubbo.service.shutdown.wait
* kill PID执行ShutdownHook。kill -9 PID不会执行ShutdownHook
* org.apache.dubbo.config.bootstrap.DubboBootstrap.destroy

#### 44、主机绑定
> 提供者
* protocolConfig.setHost("192.168.44");
* org.apache.dubbo.config.ServiceConfig.findConfigedHosts

#### 45、主机配置
> 提供者
```java
System.setProperty("DUBBO_IP_TO_REGISTRY", "192.168.44.1");
System.setProperty("DUBBO_PORT_TO_REGISTRY", "9000");
System.setProperty("DUBBO_IP_TO_BIND", "192.168.44.2");
System.setProperty("DUBBO_PORT_TO_BIND", "9001");
```
* 提供者的URL默认为registry的IP和端口，netty的ip和port存储在map
* netty监听：org.apache.dubbo.remoting.transport.AbstractServer.AbstractServer

#### 46、注册信息简化
> 提供者
* registryConfig.setSimplified(true);
* org.apache.dubbo.registry.integration.RegistryProtocol.getUrlToRegistry

#### 47、日志适配，略

#### 48、访问日志
> 备注
* org.apache.dubbo.rpc.filter.AccessLogFilter.AccessLogFilter，异步定时5s写一次磁盘
> 提供者
* protocolConfig.setAccesslog("E://test//dubbo//access.log");
* org.apache.dubbo.rpc.filter.AccessLogFilter
```text
日志示例
[2022-02-18 09:45:54] 192.168.44.1:59999 -> 192.168.44.1:28888 - hzk1/com.hzk.service.DemoService:1.0.0 sayHello(java.lang.String) ["dubbo"]
```

#### 49、服务容器
> 备注
* standalone启动web容器，或启动spring容器后拉起web容器，通过bean生命周期接口管理ServiceBean和ReferenceBean

#### 50、ReferenceConfig缓存
> 备注
* 缺省 ReferenceConfigCache 把相同服务 Group、接口、版本的 ReferenceConfig 认为是相同，缓存一份。即以服务 Group、接口、版本为缓存的 Key
* 简单来说就是合并ReferenceConfig。苍穹遮掩了ReferenceConfig的appGroup，这里也得遮掩