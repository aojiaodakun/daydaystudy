package com.hzk.java.lock.bos;



public class SyncDeadLockTest {


    public static void main(String[] args) throws Exception {
        method1();
//        method2();

        System.out.println("main end");
    }

    /**
     * 原场景是main线程处于Runnable，method1更符合原问题
     * @throws Exception
     */
    public static void method1() throws Exception {
        new Thread(()->{
            System.out.println(Thread.currentThread().getName() + " start");
            MCApiUtil.getString("a");
        }, "Config-Thread").start();

        AccountUtils.getAllTenantsByCurrentEnv();
        System.out.println("method1 end");
    }

    public static void method2() throws Exception {
        new Thread(()->{
            System.out.println("主线程开始执行 AccountUtils 方法");
            AccountUtils.getAllTenantsByCurrentEnv();
        }, "Main-Thread").start();

        new Thread(()->{
            System.out.println(Thread.currentThread().getName() + " start");
            MCApiUtil.getString("a");
        }, "Config-Thread").start();
    }

}
