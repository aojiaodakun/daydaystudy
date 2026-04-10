package com.hzk.java.proxy.jdk;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * jdk动态代理：通过反射接口实现
 * 1、接口、接口实现类
 * 2、InvocationHandle接口实现类，传入target
 * 3、java.lang.reflect.Proxy.newProxyInstance反射产生代理对象
 * 4、系统变量。sun.misc.ProxyGenerator.saveGeneratedFiles
 */
public class JDKProxyDemo {

    static {
        // sun.misc.ProxyGenerator.saveGeneratedFiles
        System.setProperty("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");
    }

    public static void main(String[] args) {
        IUserService userService = new UserServiceImpl();
        MyJDKProxyHandler handler = new MyJDKProxyHandler(userService);
        IUserService proxyInstance = (IUserService) Proxy.newProxyInstance(userService.getClass().getClassLoader(),
                userService.getClass().getInterfaces(), handler);
        System.out.println(proxyInstance.add("aaaa"));
    }





}
class MyJDKProxyHandler implements InvocationHandler {

    private Object target;

    public MyJDKProxyHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result = null;
        if (method.getName().equals("add")) {
            System.out.println("before invoke " + method.getName());
            result = method.invoke(target, args);
            System.out.println("after invoke " + method.getName());
        }
        return result;
    }
}

