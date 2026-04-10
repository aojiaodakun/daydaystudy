package com.hzk.tool.limit;

import com.google.common.util.concurrent.RateLimiter;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 限流器
 */
public class RateLimiterTest {

    private final static Executor executor = Executors.newSingleThreadExecutor();
    private final static ConcurrentHashMap<String, RateLimiter> type_limiter_map = new ConcurrentHashMap<>();
    private final static ConcurrentHashMap<String, Boolean> type_enable_map = new ConcurrentHashMap<>();
    private static int successCount = 0;
    private static int failCount = 0;



    public static void main(String[] args) throws Exception {
//        allowRequest("test1");
//        Thread.currentThread().sleep(1000);
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 200; i++) {
            allowRequest("test1");
        }
        long endTime = System.currentTimeMillis();
        System.out.println(endTime - startTime + "ms");
        System.out.println("success=" + successCount);
        System.out.println("fail=" + failCount);
        System.out.println("----------------------");
        while (true) {
            successCount = 0;
            failCount = 0;
            Thread.currentThread().sleep(1000);
            for (int i = 0; i < 200; i++) {
                allowRequest("test1");
            }
            System.out.println("success=" + successCount);
            System.out.println("fail=" + failCount);
            System.out.println("----------------------");
        }
    }

    private void limitTest1() {
        RateLimiter rateLimiter = RateLimiter.create(10);
        rateLimiter.acquire(10);
//        rateLimiter.tryAcquire();
//        Thread.currentThread().sleep(1000);
        for (int i = 0; i < 200; i++) {
            boolean flag = rateLimiter.tryAcquire();
            if (flag) {
                successCount++;
            } else {
                failCount++;
            }
        }
    }

    public static boolean allowRequest(String key) {
        // 限流器第一秒不可用，不做限流
        if (!type_enable_map.containsKey(key)) {
            getRateLimiter(key);
            successCount++;
            return true;
        }
        RateLimiter limiter = getRateLimiter(key);
        if (!limiter.tryAcquire()) {
            failCount++;
            return false;
        }
        successCount++;
        return true;
    }

    private static RateLimiter getRateLimiter(String key) {
        double permitsPerSecond = 10_000.0 / 60.0; // 转换为每秒允许的请求数
        RateLimiter limiter = type_limiter_map.computeIfAbsent(key, k -> {
            RateLimiter rateLimiter = RateLimiter.create(permitsPerSecond);
            // 异步预加载（不阻塞主线程）
            CompletableFuture.runAsync(() -> {
                // 第一秒不可用
                rateLimiter.acquire((int) permitsPerSecond);
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                type_enable_map.put(key, Boolean.TRUE);
            });
            return rateLimiter;
        });
        return limiter;
    }



}
