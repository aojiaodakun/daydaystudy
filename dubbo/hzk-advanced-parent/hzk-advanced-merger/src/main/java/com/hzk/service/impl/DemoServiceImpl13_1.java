package com.hzk.service.impl;

import com.hzk.service.IDemoService13;

import java.util.ArrayList;
import java.util.List;

public class DemoServiceImpl13_1 implements IDemoService13 {
    @Override
    public List<String> merger(String name) {
        List<String> list = new ArrayList<>();
        list.add("impl1");
        return list;
    }
    @Override
    public String sayHello(String name) {
        return null;
    }
}
