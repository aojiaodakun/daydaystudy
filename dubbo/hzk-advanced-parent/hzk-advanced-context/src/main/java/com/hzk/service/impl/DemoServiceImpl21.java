package com.hzk.service.impl;

import com.hzk.service.IDemoService21;
import org.apache.dubbo.rpc.RpcContext;

public class DemoServiceImpl21 implements IDemoService21 {
    @Override
    public String rpcContextTest(String name) {
        String divParam = RpcContext.getContext().getAttachment("divParam");
        System.out.println("隐式参数:" + divParam);
        return "rpcContextTest success:" + name;
    }

    @Override
    public String sayHello(String name) {
        return null;
    }
}
