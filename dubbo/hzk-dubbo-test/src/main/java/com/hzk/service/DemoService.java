package com.hzk.service;

import com.hzk.dto.ExampleParamDTO;
import com.hzk.callback.CallbackService;

import java.util.List;
import java.util.concurrent.CompletableFuture;

// 继承是为了实现<参数回调>
public interface DemoService extends CallbackService {

    // 同步调用
    String sayHello(String name);
    // 异步执行1
    CompletableFuture<String> asyncCall(String name);
    // 异步执行2
    String asyncCall2(String name);

    /**
     * 13、分组聚合测试
     * @param name
     * @return
     */
    List<String> merge(String name);
    // 参数验证
    String paramValid(ExampleParamDTO paramDTO);

}
