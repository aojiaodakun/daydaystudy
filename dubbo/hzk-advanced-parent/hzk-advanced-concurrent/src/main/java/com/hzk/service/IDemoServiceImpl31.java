package com.hzk.service;

import org.apache.dubbo.rpc.RpcContext;

public class IDemoServiceImpl31 implements IDemoService {
    @Override
    public String sayHello(String name) {
        String result = "Hello " + name + ", request from consumer: " + RpcContext.getContext().getRemoteAddress()
                + ", response from provider: " + RpcContext.getContext().getLocalAddress();
        System.out.println(result);
        try {
            Thread.currentThread().sleep(1000 * 5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }
}
