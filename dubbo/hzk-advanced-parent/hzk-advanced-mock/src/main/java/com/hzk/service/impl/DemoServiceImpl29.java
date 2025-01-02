package com.hzk.service.impl;

import com.hzk.exception.HzkException;
import com.hzk.service.IDemoService29;
import org.apache.dubbo.rpc.RpcException;

public class DemoServiceImpl29 implements IDemoService29 {

    // 1、return。RpcException且非业务异常
    @Override
    public String mockReturn(String name) {
        System.out.println("mockReturn entry");
        RpcException rpcException = new RpcException();
        rpcException.setCode(RpcException.UNKNOWN_EXCEPTION);
        throw rpcException;
    }

    // 2、throw。调用异常，抛出指定异常类
    @Override
    public String mockThrow(String name) {
        System.out.println("mockThrow entry");
        RpcException rpcException = new RpcException();
        rpcException.setCode(RpcException.UNKNOWN_EXCEPTION);
        throw rpcException;
    }

    // 3、force。本地强制返回，不发起远程调用，测试用例使用
    @Override
    public String mockForce(String name) {
        System.out.println("mockForce entry");
        return null;
    }

    @Override
    public String sayHello(String name) {
        return null;
    }
}
