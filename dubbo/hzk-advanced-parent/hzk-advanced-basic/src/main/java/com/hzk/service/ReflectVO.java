package com.hzk.service;

public class ReflectVO {


    public String hello() {
        return "hello";
    }
    public String hello(String name) {
        return "hello " + name;
    }

    public String hello(Integer name) {
        return "hello " + name;
    }

}
