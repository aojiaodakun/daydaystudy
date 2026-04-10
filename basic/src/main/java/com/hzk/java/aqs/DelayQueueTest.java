package com.hzk.java.aqs;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;


public class DelayQueueTest {

    // 延时任务队列
    private final DelayQueue<ScheduledTask> queue = new DelayQueue<>();
    private final ExecutorService workers;
    private final AtomicInteger threadCount = new AtomicInteger();

    public DelayQueueTest(int threadCount) {
        this.workers = Executors.newFixedThreadPool(threadCount);
        this.threadCount.set(threadCount);
        // 启动worker线程
        for (int i = 0; i < threadCount; i++) {
            workers.submit(this::workerLoop);
        }
    }

    /**
     * 提交一个周期性任务（间隔秒）
     */
    public void submit(String name, long intervalSeconds) {
        long next = System.currentTimeMillis() + intervalSeconds;
        queue.put(new ScheduledTask(name, intervalSeconds, next));
    }

    /**
     * 工作线程循环：从DelayQueue中取到期任务
     */
    private void workerLoop() {
        try {
            while (true) {
                ScheduledTask task = queue.take(); // 阻塞直到到期
                long now = System.currentTimeMillis();
                System.out.printf("[%s] 执行任务 %s @ %d%n",
                        Thread.currentThread().getName(), task.name, now);

                // 模拟执行耗时
                Thread.sleep(100);

                // 重新调度下次执行（保持周期不漂移）
                long nextTime = task.scheduledTime + task.intervalSeconds * 1000;
                queue.put(new ScheduledTask(task.name, task.intervalSeconds, nextTime));
            }
        } catch (InterruptedException ignored) {
        }
    }

    /**
     * 延时任务类
     */
    private static class ScheduledTask implements Delayed {
        final String name;
        final long intervalSeconds;
        final long scheduledTime;

        public ScheduledTask(String name, long intervalSeconds, long scheduledTime) {
            this.name = name;
            this.intervalSeconds = intervalSeconds;
            this.scheduledTime = scheduledTime;
        }

        @Override
        public long getDelay(TimeUnit unit) {
            return unit.convert(scheduledTime - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
        }

        @Override
        public int compareTo(Delayed o) {
            return Long.compare(this.scheduledTime, ((ScheduledTask) o).scheduledTime);
        }
    }

    public static void main(String[] args) {
        DelayQueueTest scheduler = new DelayQueueTest(3); // 3个工作线程

        // 提交10个500ms周期任务
        for (int i = 1; i <= 10; i++) {
            scheduler.submit("Task-" + i, 0); // 立即执行
        }

        // 模拟周期：500ms
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            for (int i = 1; i <= 10; i++) {
                scheduler.submit("Task-" + i, 500);
            }
        }, 0, 500, TimeUnit.MILLISECONDS);
    }

}
