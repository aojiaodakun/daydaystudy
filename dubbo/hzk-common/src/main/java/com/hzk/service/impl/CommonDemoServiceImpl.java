package com.hzk.service.impl;

import com.hzk.service.IDemoService;
import org.apache.dubbo.rpc.RpcContext;

public class CommonDemoServiceImpl implements IDemoService {

    @Override
    public String sayHello(String name) {
        String result = "Hello " + name + ", request from consumer: " + RpcContext.getContext().getRemoteAddress()
                + ", response from provider: " + RpcContext.getContext().getLocalAddress();
        System.out.println(result);
        return result;
    }

}
