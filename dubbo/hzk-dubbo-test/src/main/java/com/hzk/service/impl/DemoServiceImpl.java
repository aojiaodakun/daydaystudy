package com.hzk.service.impl;

import com.hzk.dto.ExampleParamDTO;
import com.hzk.callback.CallbackListener;
import com.hzk.service.DemoService;
import org.apache.dubbo.rpc.AsyncContext;
import org.apache.dubbo.rpc.RpcContext;
import org.apache.dubbo.rpc.RpcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DemoServiceImpl implements DemoService {

    private static final Logger logger = LoggerFactory.getLogger(DemoServiceImpl.class);

    private static final ExecutorService myExecutorService = Executors.newCachedThreadPool();

    private final Map<String, CallbackListener> LISTENER_MAP = new ConcurrentHashMap<>();

    public DemoServiceImpl(){
        Thread t = new Thread(new Runnable() {
            public void run() {
                while(true) {
                    try {
                        for(Map.Entry<String, CallbackListener> entry : LISTENER_MAP.entrySet()){
                            try {
                                // 发送变更通知
                                entry.getValue().changed(getChanged(entry.getKey()));
                            } catch (Throwable t) {
                                LISTENER_MAP.remove(entry.getKey());
                            }
                        }
                        Thread.sleep(5000); // 定时触发变更通知
                    } catch (Throwable t) { // 防御容错
                        t.printStackTrace();
                    }
                }
            }
        });
        t.setDaemon(true);
        t.start();
    }
    @Override
    public void addListener(String key, CallbackListener listener) {
        LISTENER_MAP.put(key, listener);
        // 单次回调
//        listener.changed(getChanged(key));
    }

    private String getChanged(String key) {
        return "Changed: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }


    @Override
    public String sayHello(String name) {
        logger.info("Hello " + name + ", request from consumer: " + RpcContext.getContext().getRemoteAddress());
        RpcContext rpcContext = RpcContext.getContext();
        String result = "Hello " + name + ", response from provider: " + RpcContext.getContext().getLocalAddress() + ",registryPort:" + rpcContext.getAttachment("registryPort");
        System.out.println(result);

        if (name.equals("sleep")) {
            try {
                Thread.currentThread().sleep(1000 * 5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (name.equals("exception")) {
            RpcException rpcException = new RpcException();
            rpcException.setCode(RpcException.BIZ_EXCEPTION);
            rpcException.setCode(RpcException.LIMIT_EXCEEDED_EXCEPTION);
            throw rpcException;
        }
        return result;
    }

    @Override
    public CompletableFuture<String> asyncCall(String name) {
        RpcContext savedContext = RpcContext.getContext();
        // 建议为supplyAsync提供自定义线程池，避免使用JDK公用线程池
//        return CompletableFuture.supplyAsync(() -> {
//            System.out.println(savedContext.getAttachment("consumer-key1"));
//            try {
//                Thread.sleep(5000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            return "async response from provider。impl1";
//        });

        // 使用业务自定义线程池
        return CompletableFuture.supplyAsync(() -> {
            System.out.println(savedContext.getAttachment("consumer-key1"));
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "async response from provider。impl1";
        }, myExecutorService);
    }

    @Override
    public String asyncCall2(String name) {
        final AsyncContext asyncContext = RpcContext.startAsync();
        new Thread(() -> {
            // 如果要使用上下文，则必须要放在第一句执行
            asyncContext.signalContextSwitch();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // 写回响应
            asyncContext.write("Hello " + name + ", response from provider.impl1");
        }).start();
        return null;
    }


    @Override
    public List<String> merge(String name) {
        List<String> list = new ArrayList<>();
        list.add("impl1");
        return list;
    }

    @Override
    public String paramValid(ExampleParamDTO paramDTO) {
        return "true";
    }


}