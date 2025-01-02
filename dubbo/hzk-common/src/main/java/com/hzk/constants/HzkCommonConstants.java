package com.hzk.constants;

import org.apache.dubbo.rpc.protocol.dubbo.DubboProtocol;

public interface HzkCommonConstants {
    // 注册中心配置
    String REGISTRY_ZK_2181 = "zookeeper://127.0.0.1:2181";
    String REGISTRY_ZK_2182 = "zookeeper://127.0.0.1:2182";
    // 应用配置
    String APPLICATION_NAME_PROVIDER = "hzk-demo-provider";
    String APPLICATION_NAME_CONSUMER = "hzk-demo-consumer";
    // 协议配置
    String PROTOCOL_DUBBO = "dubbo";
    String PROTOCOL_HTTP = "http";
    // 端口
    int PROTOCOL_DUBBO_DEFAULT_PORT = DubboProtocol.DEFAULT_PORT;





}
