package com.hzk.mock;

import com.hzk.callback.CallbackListener;
import com.hzk.dto.ExampleParamDTO;
import com.hzk.service.DemoService;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class DemoServiceMock implements DemoService{

    /**
     * 29、本地伪装测试
     * @param name
     * @return
     */
    @Override
    public String sayHello(String name) {
        return "容错数据";
    }

    @Override
    public CompletableFuture<String> asyncCall(String name) {
        return null;
    }

    @Override
    public String asyncCall2(String name) {
        return null;
    }

    @Override
    public List<String> merge(String name) {
        return null;
    }

    @Override
    public String paramValid(ExampleParamDTO paramDTO) {
        return null;
    }

    @Override
    public void addListener(String key, CallbackListener listener) {
        return;
    }
}
