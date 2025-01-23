package com.hzk.service;

import org.apache.dubbo.rpc.RpcException;

public interface IDemoService {

    String sayHello(String name);

    default void sleep() {
        try {
            Long aLong = Long.getLong("dubbo.test.sleep", 100);
            Thread.sleep(aLong);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("provider sleep");
    }

}
