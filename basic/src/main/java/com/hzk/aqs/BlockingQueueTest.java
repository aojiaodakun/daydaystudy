package com.hzk.aqs;

import org.junit.Test;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * 了解阻塞队列api
 * 异常API、布尔API、阻塞API
 */
public class BlockingQueueTest {

    /**
     * 异常API
     */
    @Test
    public void exceptionApiTest(){
        BlockingQueue<String> blockingQueue = new ArrayBlockingQueue<>(3);
        System.out.println(blockingQueue.add("a"));
        System.out.println(blockingQueue.add("b"));
        System.out.println(blockingQueue.add("c"));
        // 移除
        System.out.println(blockingQueue.remove());
        System.out.println(blockingQueue.remove());
        System.out.println(blockingQueue.remove());
        System.out.println(blockingQueue.remove());
    }

    /**
     * 布尔API
     */
    @Test
    public void booleanApi() throws Exception{
        BlockingQueue<String> blockingQueue = new ArrayBlockingQueue<>(3);
        System.out.println(blockingQueue.offer("a"));
        System.out.println(blockingQueue.offer("b"));
        System.out.println(blockingQueue.offer("c"));
        System.out.println(blockingQueue.offer("d",2,TimeUnit.SECONDS));
        // 偷窥
        System.out.println(blockingQueue.peek());
        // 获取
        System.out.println(blockingQueue.poll());
        System.out.println(blockingQueue.poll());
        System.out.println(blockingQueue.poll());
        System.out.println(blockingQueue.poll());
    }

    /**
     * 阻塞API
     */
    @Test
    public void blockApi() throws Exception{
        BlockingQueue<String> blockingQueue = new ArrayBlockingQueue<>(2);
        blockingQueue.put("a");
        System.out.println("put a");
        blockingQueue.put("b");
        System.out.println("put b");

        new Thread(()->{
            try {
                TimeUnit.SECONDS.sleep(5);
                blockingQueue.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        blockingQueue.put("c");
        System.out.println("put c");
    }

}
