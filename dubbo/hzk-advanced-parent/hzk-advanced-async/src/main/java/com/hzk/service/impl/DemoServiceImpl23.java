package com.hzk.service.impl;

import com.hzk.service.IDemoService23;
import org.apache.dubbo.rpc.AsyncContext;
import org.apache.dubbo.rpc.RpcContext;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DemoServiceImpl23 implements IDemoService23 {

    private static final ExecutorService myExecutorService = Executors.newCachedThreadPool();

    @Override
    public CompletableFuture<String> asyncCall1(String name) {
        // 使用业务自定义线程池
        return CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000 * 5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "async response from provider#asyncCall1";
        }, myExecutorService);
    }

    @Override
    public String asyncCall2(String name) {
        final AsyncContext asyncContext = RpcContext.startAsync();
        new Thread(() -> {
            // 如果要使用上下文，则必须要放在第一句执行
            asyncContext.signalContextSwitch();
            try {
                Thread.sleep(1000 * 5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // 写回响应
            asyncContext.write("Hello " + name + ", response from provider#asyncCall2");
        }).start();
        return null;
    }

    @Override
    public String sayHello(String name) {
        String result = "Hello " + name + ", request from consumer: " + RpcContext.getContext().getRemoteAddress()
                + ", response from provider: " + RpcContext.getContext().getLocalAddress();
        System.out.println(result);
        try {
            Thread.sleep(1000 * 5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }
}
