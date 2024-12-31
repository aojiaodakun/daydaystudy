package com.hzk.thread;

import org.junit.Test;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 了解线程池7参
 * 1、自定义线程池工厂
 * 2、使用其他阻塞队列
 */
public class ThreadPoolTest {

    /**
     * 1、核心线程数
     * 2、最大线程池
     * 3、保留时间数值
     * 4、保留时间单位
     * 5、阻塞队列
     * 6、线程池工厂
     * 7、拒绝策略
     */
    private static ExecutorService executorService = new ThreadPoolExecutor(1, 3,
            10L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(2)
            ,  new CustomThreadFactory("newtest-pool"), new ThreadPoolExecutor.DiscardOldestPolicy());

    /**
     * 1、改线程池的名字
     * 2、错误修正
     * 3、jdk默认提供4钟拒绝策略，请切换测试理解差异
        * 3,1、DiscardPolicy方式，丢弃新任务，不会抛出异常
        * 3,2、AbortPolicy方式，丢弃新任务，抛出异常java.util.concurrent.RejectedExecutionException
        * 3.3、DiscardOldestPolicy，丢第一个排队任务
        * 3.4、CallerRunsPolicy，提交任务的线程自己执行
     * 4、使用其他阻塞队列，java.util.concurrent.ArrayBlockingQueue
     * 5、了解core和max的作用
     * 6、了解未使用的线程什么时候回收
     * 自定义线程池工厂
     * @throws Exception
     */
    public static class CustomThreadFactory implements ThreadFactory {
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;

        public CustomThreadFactory(String namePrefix) {
            this.namePrefix = namePrefix;
        }

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, namePrefix + "-thread-" + threadNumber.getAndIncrement());
        }
    }

    /**
     * 异步执行runnable，无法获取返回值
     */
    @Test
    public void testRunnable() throws Exception {
        executorService.execute(()->{
            printAndSleep("AAA");
        });

        executorService.execute(()->{
            printAndSleep("BBB");
        });

        executorService.execute(()->{
            printAndSleep("CCC");
        });

        // 触发拒绝策略，调整maxPoolSize
        executorService.execute(()->{
            printAndSleep("DDD");
        });

        executorService.execute(()->{
            printAndSleep("EEE");
        });

        executorService.execute(()->{
            printAndSleep("FFF");
        });
        // 阻塞main线程
        System.in.read();
    }

    /**
     * 异步执行callable，future.get()可获取返回值
     */
    @Test
    public void testCallable() throws Exception {
        Future<String> future1 = executorService.submit(()->{
            return callableMethod("AAA");
        });

        Future<String> future2 = executorService.submit(()->{
            return callableMethod("BBB");
        });

        Future<String> future3 = executorService.submit(()->{
            return callableMethod("CCC");
        });

        System.out.println("main");
        System.out.println(future1.get(3, TimeUnit.SECONDS));

        // 阻塞main线程
        System.in.read();
    }

    /**
     * 模拟OOM
     * JVM参数:-Xmx10m
     */
    public static void main(String[] args) throws Exception{
        // 阻塞队列无界，会出现OOM。声明线程池时必须声明有界阻塞队列
        ExecutorService blockQueueOOM_Pool = Executors.newFixedThreadPool(1);
        int size = 1000 * 1000;
        for (int i = 0; i < size; i++) {
            int finalI = i;
            blockQueueOOM_Pool.execute(()->{
                printAndSleep(finalI +"");
            });
        }
        // 阻塞main线程
        System.in.read();
    }

    private static void printAndSleep(String str){
        System.out.println(Thread.currentThread().getName() + "," + str);
        try {
            // 卡住线程
//            TimeUnit.SECONDS.sleep(Integer.MAX_VALUE);
            TimeUnit.SECONDS.sleep(5);
        } catch (Exception ignored) {

        }
    }


    private static String callableMethod(String str){
        System.out.println(Thread.currentThread().getName() + "," + str);
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (Exception ignored) {

        }
        return Thread.currentThread().getName();
    }

}



