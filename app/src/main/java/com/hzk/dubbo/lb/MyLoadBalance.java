package com.hzk.dubbo.lb;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.RpcException;

import java.util.List;

public class MyLoadBalance implements org.apache.dubbo.rpc.cluster.LoadBalance {

    public MyLoadBalance(){
        System.out.println("MyLoadBalance");
    }

    public static final String NAME = "mydiv";

    @Override
    public <T> Invoker<T> select(List<Invoker<T>> invokers, URL url, Invocation invocation) throws RpcException {
        return invokers.get(0);
    }
}
