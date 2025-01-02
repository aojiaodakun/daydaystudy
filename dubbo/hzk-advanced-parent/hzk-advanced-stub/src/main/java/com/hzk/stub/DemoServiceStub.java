package com.hzk.stub;

import com.hzk.service.IDemoService;


public class DemoServiceStub implements IDemoService {

    private IDemoService proxy;

    public DemoServiceStub(IDemoService demoService){
        proxy = demoService;
    }


    @Override
    public String sayHello(String name) {
        if (!name.equals("hzk")) {
            System.out.println("参数异常");
            return "please check param";
        }
        // 发起远程调用
        return proxy.sayHello(name);
    }

}
