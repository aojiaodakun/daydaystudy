package com.hzk.java.thread;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Function;

public class FutureTest {

    public static void main(String[] args) throws Exception {
        System.out.println("main start");
        Future<String> stringFuture = calculateAsync();

//        String result = stringFuture.get(1, TimeUnit.SECONDS);
//        System.out.println(result);

        CompletableFuture<String> completableFuture
                = CompletableFuture.supplyAsync(() -> "Hello");
//        CompletableFuture<Void> future = completableFuture
//                .thenAccept(s -> System.out.println("Computation returned: " + s));

//        CompletableFuture<Void> future = completableFuture
//            .thenApply(s -> {
//                System.out.println("Computation returned: " + s);
//                return null;
//            });

//        CompletableFuture<String> future = completableFuture
//                .handle((obj, t)->{
//                    System.out.println(obj);
//                    return obj + 1;
//                });

//        CompletableFuture<String> future = CompletableFuture.completedFuture("hzk").thenApply(s -> s + 1);
        CompletableFuture<String> future = CompletableFuture.completedFuture("hzk").thenApply(Function.identity());
        String s = future.get();
        System.out.println(s);

//        CompletableFuture<Void> future = completableFuture
//                .thenRun(() -> System.out.println("Computation finished."));

//        future.get();

        System.out.println("main end");

    }

    public static Future<String> calculateAsync() throws InterruptedException {
        CompletableFuture<String> completableFuture = new CompletableFuture<>();

        Executors.newCachedThreadPool().submit(() -> {
            Thread.sleep(1000 * 5);
            completableFuture.complete("Hello");
            return null;
        });

        return completableFuture;
    }
}
