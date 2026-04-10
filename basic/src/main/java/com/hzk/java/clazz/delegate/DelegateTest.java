package com.hzk.java.clazz.delegate;

public class DelegateTest {

    public static void main(String[] args) throws Exception{
        // 查看String类的加载器
        Class clazz = Class.forName("java.lang.String");
        ClassLoader classLoader = clazz.getClassLoader();
        System.out.println(classLoader);
    }

}
