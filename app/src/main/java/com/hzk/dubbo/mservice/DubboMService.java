package com.hzk.dubbo.mservice;

public class DubboMService {

    public String hello() {
        return "hello";
    }

    public String hello(String name){
        return "name is  " + name;
//        throw new RuntimeException("服务不可用");
    }
    public String hello(Integer age){
        return "age is " + age;
    }
    public String helloe(String age){
        throw new RuntimeException("服务不可用");
    }


}
