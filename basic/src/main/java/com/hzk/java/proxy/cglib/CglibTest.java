package com.hzk.java.proxy.cglib;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class CglibTest {

    static
    {
        System.setProperty("cglib.debugLocation", "D:\\workspace\\IDEA\\basic\\target\\classes");
    }


    public static void main(String[] args) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(Student.class);
        enhancer.setCallback(new MethodInterceptor() {
            @Override
            public Object intercept(Object obj, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
                System.out.println("Before: "+ method);
                Object object = methodProxy.invokeSuper(obj, args);
                System.out.println("After: "+method);
                return object;
            }
        });

        Student student = (Student)enhancer.create();
        student.speak("hzk");

    }

    private static interface Person{
        String speak(String name);
        void walk();
    }

    private static class Student implements Person{

        public Student(){}

        @Override
        public String speak(String name) {
            System.out.println("i am student ,i can speak to:" + name);
            return "hello:" + name;
        }

        @Override
        public void walk() {
            System.out.println("i am student ,i can walk");
        }
    }
}
