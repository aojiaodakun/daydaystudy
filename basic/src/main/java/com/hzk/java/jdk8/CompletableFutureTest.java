package com.hzk.java.jdk8;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Supplier;

public class CompletableFutureTest {

    private static final ExecutorService THREADPOOL = Executors.newFixedThreadPool(5);

    public static void main(String[] args) throws Exception{

        List<CompletableFuture<String>> futureList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            int finalInt = i;
            CompletableFuture<String> future = CompletableFuture.supplyAsync(new MyTask(finalInt), THREADPOOL);
            // 正常流程
            future.thenApply(e->{
                System.out.println("正常流程:" + finalInt);
                return "success";
            });

            // 异常流程
            future.exceptionally(e->{
                System.err.println("异常流程:" + finalInt);
                // TODO 打印日志
                return "error";
            });
            futureList.add(future);
        }
        List<String> resultList = new ArrayList<>();
        for (int i = 0; i < futureList.size(); i++) {
            try {
                String result = futureList.get(i).get();
                resultList.add(result);
            } catch (RuntimeException | ExecutionException e) {
                // 忽略
            }
        }
        System.in.read();
    }

}
class MyTask implements Supplier<String> {

    private long targetId;

    public MyTask(long targetId) {
        this.targetId = targetId;
    }

    @Override
    public String get()  {
        System.out.println("call,taskId:" + targetId);
        try {
            Thread.currentThread().sleep(1000 * 5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (targetId == 3) {
            throw new RuntimeException();
        }
        return "call result:" + targetId;
    }

}


