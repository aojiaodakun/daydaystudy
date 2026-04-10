package com.hzk.java.thread;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

public class CallableDemo {

    public static void main(String[] args) throws Exception {
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        FutureTask<Integer> futureTask = new FutureTask<>(new CallableData());


        executorService.execute(futureTask);
//        Thread thread = new Thread(futureTask);
//        thread.start();
        System.out.println(futureTask.get());
    }

}
class CallableData implements Callable<Integer> {

    @Override
    public Integer call() throws Exception {
        System.out.println("callable running...");
        return 1024;
    }

}