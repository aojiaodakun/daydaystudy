package com.hzk.java.lock.bos;

import java.util.concurrent.TimeUnit;


public class AccountUtils {

    public static synchronized void getAllTenantsByCurrentEnv() {
        System.out.println(Thread.currentThread().getName() + ": 获取了 AccountUtils 锁");
        try {
            System.out.println("AccountUtils#init start...");
            TimeUnit.MILLISECONDS.sleep(10);

            // TODO，MCApiUtil.getString阻塞
            MCApiUtil.getString("a");
            // 当前类的getString，正常
            getString("a");

            System.out.println("AccountUtils#init end...");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + ": 释放了 AccountUtils 锁");
    }


    public static String getString(Object object) {
        return object == null ? null : String.valueOf(object);
    }


}
