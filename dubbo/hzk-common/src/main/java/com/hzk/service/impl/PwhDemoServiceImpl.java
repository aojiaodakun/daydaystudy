package com.hzk.service.impl;

import com.hzk.service.IDemoService;
import org.apache.dubbo.rpc.RpcContext;

public class PwhDemoServiceImpl implements IDemoService {

    public String sayHello(String name) {
        String result = "Hellopwh " + name + ", request from consumer: " + RpcContext.getContext().getRemoteAddress()
                + ", response from provider: " + RpcContext.getContext().getLocalAddress();
        System.out.println(result);
        return result;
    }
}
