package com.hzk.pigeon.sch;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * PigeonSch 调度器
 *
 * - 支持多桶任务分发，每个桶周期触发执行
 * - 使用 DelayQueue 控制桶触发时间
 * - 使用线程池复用执行线程，无需手动创建销毁
 * - 使用 Power-of-Two-Choices（P2C） 动态选择低负载桶
 * - 支持特殊桶（例如 fast / cleanup）
 */
public class PigeonSch {

    /** 桶触发控制队列 */
    private final DelayQueue<Bucket> delayQueue = new DelayQueue<>();

    /** 所有桶列表（包括特殊桶） */
    private final List<Bucket> buckets = new ArrayList<>();

    /** 普通任务桶，用于 P2C 分配 */
    private final List<Bucket> normalBuckets = new ArrayList<>();

    /** 线程池（统一复用线程） */
    private final ExecutorService executor = Executors.newFixedThreadPool(
            Math.max(4, Runtime.getRuntime().availableProcessors() * 2));

    /** 随机用于P2C采样 */
    private final Random random = new Random();

    /** 默认桶数量 */
    private final int bucketCount;

    public PigeonSch(int bucketCount) {
        this.bucketCount = bucketCount;
        initBuckets();
        startDispatcher();
    }

    /** 初始化桶，包括特殊桶 */
    private void initBuckets() {
        // 初始化普通桶
        for (int i = 0; i < bucketCount; i++) {
            Bucket b = new Bucket("bucket-" + i, System.currentTimeMillis());
            buckets.add(b);
            normalBuckets.add(b);
            delayQueue.offer(b);
        }

        // 添加固定周期任务桶
        Bucket fast = new Bucket("fast", System.currentTimeMillis(), 500);
        Bucket cleanup = new Bucket("cleanup", System.currentTimeMillis(), 3600_000);

        buckets.add(fast);
        buckets.add(cleanup);

        delayQueue.offer(fast);
        delayQueue.offer(cleanup);

        System.out.println("[Init] 普通桶: " + bucketCount + "，特殊桶: fast, cleanup");
    }

    /**
     * 提交任务（普通任务）
     */
    public void submit(Task task, long initialDelayMs) {
        long triggerTime = System.currentTimeMillis() + initialDelayMs;
        Bucket bucket = chooseBucketP2C();
        bucket.addTask(new TaskWrapper(task, triggerTime));
    }

    /**
     * 提交到指定桶（如 fast / cleanup）
     */
    public void submitToBucket(String bucketName, Task task, long initialDelayMs) {
        for (Bucket b : buckets) {
            if (b.name.equals(bucketName)) {
                b.addTask(new TaskWrapper(task, System.currentTimeMillis() + initialDelayMs));
                return;
            }
        }
        throw new IllegalArgumentException("No such bucket: " + bucketName);
    }

    /**
     * 主调度线程，从 DelayQueue 中取桶并派发执行
     */
    private void startDispatcher() {
        Thread dispatcher = new Thread(() -> {
            while (true) {
                try {
                    Bucket bucket = delayQueue.take(); // 阻塞直到桶到期
                    long now = System.currentTimeMillis();
                    List<TaskWrapper> dueTasks = bucket.drainDueTasks(now);
                    if (!dueTasks.isEmpty()) {
                        // 异步派发任务执行（线程池复用）
                        executor.submit(() -> {
                            for (TaskWrapper w : dueTasks) {
                                try {
                                    w.task.run();
                                    // 执行完后重新放回桶，准备下一次
                                    bucket.addTask(new TaskWrapper(w.task, now + w.task.nextIntervalMs()));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                    // 重设桶的下一次触发时间
                    bucket.resetNextTrigger();
                    delayQueue.offer(bucket);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }, "Pigeon-Dispatcher");
        dispatcher.setDaemon(true);
        dispatcher.start();
    }

    /**
     * Power-of-Two-Choices 动态选择负载较轻的桶
     */
    private Bucket chooseBucketP2C() {
        Bucket a = normalBuckets.get(random.nextInt(normalBuckets.size()));
        Bucket b = normalBuckets.get(random.nextInt(normalBuckets.size()));
        return a.load.get() <= b.load.get() ? a : b;
    }

    // ======================== 内部类 ========================

    /**
     * 桶定义
     */
    static class Bucket implements Delayed {
        final String name;
        final Queue<TaskWrapper> queue = new ConcurrentLinkedQueue<>();
        final AtomicInteger load = new AtomicInteger(0);
        private long nextTriggerTime;
        private final long fixedInterval; // 特殊桶固定间隔（ms）

        Bucket(String name, long startTime) {
            this(name, startTime, 1000); // 默认1秒
        }

        Bucket(String name, long startTime, long interval) {
            this.name = name;
            this.nextTriggerTime = startTime + interval;
            this.fixedInterval = interval;
        }

        void addTask(TaskWrapper w) {
            queue.offer(w);
            load.incrementAndGet();
        }

        List<TaskWrapper> drainDueTasks(long now) {
            List<TaskWrapper> list = new ArrayList<>();
            for (Iterator<TaskWrapper> it = queue.iterator(); it.hasNext(); ) {
                TaskWrapper w = it.next();
                if (w.triggerTime <= now) {
                    list.add(w);
                    it.remove();
                    load.decrementAndGet();
                }
            }
            return list;
        }

        void resetNextTrigger() {
            this.nextTriggerTime = System.currentTimeMillis() + fixedInterval;
        }

        @Override
        public long getDelay(TimeUnit unit) {
            return unit.convert(nextTriggerTime - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
        }

        @Override
        public int compareTo(Delayed o) {
            return Long.compare(this.nextTriggerTime, ((Bucket) o).nextTriggerTime);
        }
    }

    /**
     * 任务包装
     */
    static class TaskWrapper {
        final Task task;
        final long triggerTime;
        TaskWrapper(Task task, long triggerTime) {
            this.task = task;
            this.triggerTime = triggerTime;
        }
    }

    /**
     * 任务接口
     */
    public interface Task {
        String getName();
        void run();
        long nextIntervalMs(); // 返回下次调度间隔
    }

    // ======================== 测试入口 ========================

    public static void main(String[] args) throws Exception {
        PigeonSch sch = new PigeonSch(4);
        Map<String, AtomicInteger> taskName2countMap = new ConcurrentHashMap<>();
        new Thread(()->{
            while (true) {
                try {
                    Thread.currentThread().sleep(1000 * 1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.err.println(taskName2countMap);
            }
        }).start();

        // 模拟普通任务
        for (int i = 0; i < 10; i++) {
            int id = i;
            String taskName = "bigMessage-" + id;
            taskName2countMap.put(taskName, new AtomicInteger(0));
            sch.submit(new Task() {
                @Override
                public String getName() {
                    return taskName;
                }

                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName() + " 执行普通任务-" + id);
                    taskName2countMap.get(taskName).getAndIncrement();
                }
                @Override
                public long nextIntervalMs() { return 1000 + new Random().nextInt(1000); }
            }, 1000);
        }

        // 模拟fast任务
        sch.submitToBucket("fast", new Task() {
            @Override
            public String getName() {
                return "fast";
            }

            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + " 执行fast任务");
            }
            @Override
            public long nextIntervalMs() { return 500; }
        }, 0);

        // 模拟cleanup任务
        sch.submitToBucket("cleanup", new Task() {
            @Override
            public String getName() {
                return "cleanup";
            }
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + " 执行cleanup任务");
            }
            @Override
            public long nextIntervalMs() { return 3600_000; }
        }, 0);

        System.in.read();
    }
}

