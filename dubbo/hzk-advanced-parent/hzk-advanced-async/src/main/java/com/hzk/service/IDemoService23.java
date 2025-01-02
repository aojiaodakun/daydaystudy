package com.hzk.service;

import java.util.concurrent.CompletableFuture;

public interface IDemoService23 extends IDemoService {

    // 异步执行1
    CompletableFuture<String> asyncCall1(String name);
    // 异步执行2
    String asyncCall2(String name);

}
