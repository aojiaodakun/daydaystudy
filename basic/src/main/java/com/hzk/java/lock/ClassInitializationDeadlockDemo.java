package com.hzk.java.lock;

public class ClassInitializationDeadlockDemo {

    public static void main(String[] args) {
        System.out.println("应用程序启动...");

        // 主线程执行 AccountUtils 的方法
        new Thread(() -> {
            System.out.println("主线程开始执行 AccountUtils 方法");
            AccountUtils.getAllTenantsByCurrentEnv();
        }, "Main-Thread").start();

        // 模拟另一个线程初始化 MCApiUtil
        new Thread(() -> {
            try {
                // 稍微延迟，确保主线程先获取锁
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("配置线程开始初始化 MCApiUtil");
            MCApiUtil.doSomething();
        }, "Config-Thread").start();

        // 等待一段时间让死锁发生
        try {
            Thread.sleep(5000);
            System.out.println("应用程序似乎卡住了 - 这就是死锁现象!");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class AccountUtils {
    // 模拟同步方法
    public static synchronized void getAllTenantsByCurrentEnv() {
        System.out.println(Thread.currentThread().getName() + ": 获取了 AccountUtils 锁");

        try {
            // 模拟耗时操作
            Thread.sleep(1000);

            // 这里调用 MCApiUtil 的方法，触发其类初始化
            System.out.println(Thread.currentThread().getName() + ": 准备调用 MCApiUtil 方法");
            MCApiUtil.doSomething();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(Thread.currentThread().getName() + ": 释放 AccountUtils 锁");
    }
}

class MCApiUtil {
    // 静态初始化块 - 这里模拟问题场景
    static {
        System.out.println(Thread.currentThread().getName() + ": MCApiUtil 类初始化开始");

        // 这里调用 AccountUtils 方法，尝试获取其锁
        // 这就是导致死锁的原因
        System.out.println(Thread.currentThread().getName() + ": 在静态初始化中调用 AccountUtils 方法");
        AccountUtils.getAllTenantsByCurrentEnv();

        System.out.println(Thread.currentThread().getName() + ": MCApiUtil 类初始化完成");
    }

    public static void doSomething() {
        System.out.println(Thread.currentThread().getName() + ": MCApiUtil.doSomething() 被调用");
    }
}