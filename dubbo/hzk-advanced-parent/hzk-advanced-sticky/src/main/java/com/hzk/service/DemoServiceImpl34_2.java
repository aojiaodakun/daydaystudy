package com.hzk.service;

public class DemoServiceImpl34_2 implements IDemoService {

    @Override
    public String sayHello(String name) {
        return "hello " + name + " from " + this.getClass().getSimpleName();
    }
}