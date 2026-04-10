package com.hzk.datastructure;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 500毫秒/100条，批量处理
 */
public class AsyncQueueThreadTest {


    public static void main(String[] args) throws Exception {
        Thread thread = new AsyncQueueThread();
        thread.start();

        new Thread(()->{
            for (int i = 0; i < 5000; i++) {
                AsyncQueueThread.putMessage("" + i);
            }

            while (true) {
                try {
                    Thread.currentThread().sleep(10 * 5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                AsyncQueueThread.putMessage("a");
            }


        }).start();

        System.in.read();

    }


}
class AsyncQueueThread extends Thread{

    private static class Item {
        long addTime;
        String message;

        public Item(long addTime, String message) {
            this.addTime = addTime;
            this.message = message;
        }
    }

    private static final BlockingQueue<Item> messageQueue = new LinkedBlockingDeque<>(10000);
    private static int batchSize = 100;

    static void putMessage(String message) {
        try {
            messageQueue.put(new Item(System.currentTimeMillis(), message));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (true) {
            Item firstItem = null;
            try {
                firstItem = messageQueue.poll(500, TimeUnit.MILLISECONDS);
                if (firstItem == null) {
                    continue;
                }
                long spanTime = System.currentTimeMillis() - firstItem.addTime;
                if (spanTime < 500) {
                    Thread.sleep(500 - spanTime);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int currBatchSize = Math.min(messageQueue.size(), batchSize);
            List<String> messageList = new ArrayList<>(currBatchSize);
            messageList.add(firstItem.message);
            for (int i = 0; i < currBatchSize - 1; i++) {
                Item item = messageQueue.poll();
                String tempMessage = item.message;
                messageList.add(tempMessage);
            }
            System.out.println(messageList);
        }
    }
}