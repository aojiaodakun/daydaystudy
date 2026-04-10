package com.hzk.java.lock.bos;

public class MCApiUtil {


    static {
        System.out.println(Thread.currentThread().getName() + ": MCApiUtil 类初始化开始");
        AccountUtils.getAllTenantsByCurrentEnv();
        System.out.println(Thread.currentThread().getName() + ": MCApiUtil 类初始化完成");
    }

    public static String getString(Object object) {
        return object == null ? null : String.valueOf(object);
    }

}
