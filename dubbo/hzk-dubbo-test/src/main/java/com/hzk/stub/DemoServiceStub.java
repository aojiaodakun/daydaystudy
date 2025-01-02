package com.hzk.stub;

import com.hzk.callback.CallbackListener;
import com.hzk.dto.ExampleParamDTO;
import com.hzk.service.DemoService;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class DemoServiceStub implements DemoService {

    private DemoService proxy;

    public DemoServiceStub(DemoService demoService){
        proxy = demoService;
    }


    @Override
    public String sayHello(String name) {
        /**
         * 28、本地存根测试
         */
        if (!name.equals("hzk")) {
            System.out.println("参数异常");
            return null;
        }
        return proxy.sayHello(name);
    }

    @Override
    public CompletableFuture<String> asyncCall(String name) {
        return proxy.asyncCall(name);
    }

    @Override
    public String asyncCall2(String name) {
        return proxy.asyncCall2(name);
    }

    @Override
    public List<String> merge(String name) {
        return proxy.merge(name);
    }

    @Override
    public String paramValid(ExampleParamDTO paramDTO) {
        return proxy.paramValid(paramDTO);
    }

    @Override
    public void addListener(String key, CallbackListener listener) {
        proxy.addListener(key, listener);
    }
}
