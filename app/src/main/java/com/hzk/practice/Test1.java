package com.hzk.practice;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class Test1 {

    public static void main(String[] args) throws Exception {

        StudentVO studentVO = new StudentVO();
        studentVO.methodA();
//       StudentVO instance = StudentVO.getInstance();
        StudentVO.getInstance();
//        studentVO.print();

        /*反射
        * */


        Class<?> aClass = Class.forName("com.hzk.practice.StudentVO");
        Object obj = aClass.getDeclaredConstructor().newInstance();
        // 属性
        Field field = aClass.getDeclaredField("name");
//        私有的需要设置可访问
        field.setAccessible(true);

        Field field1 = aClass.getDeclaredField("age");
        field1.setAccessible(true);
        Object o = field1.get(null);

        Object value = field.get(obj);
        field.set(obj,"pwh");
        Object value1 = field.get(obj);
        // 方法
        Method method = aClass.getMethod("getInstance");
        // 静态方法不需要传obj
        Object invoke = method.invoke(null);

        Method methodb = aClass.getMethod("print");
        methodb.invoke(obj);


    }

}
