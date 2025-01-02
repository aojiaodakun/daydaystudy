package com.hzk.service.impl;

import org.apache.dubbo.rpc.service.GenericException;
import org.apache.dubbo.rpc.service.GenericService;

public class MyGenericServiceImpl implements GenericService {
    @Override
    public Object $invoke(String methodName, String[] parameterTypes, Object[] args) throws GenericException {
        if ("sayHello".equals(methodName)) {
            System.out.println("Welcome " + args[0]);
            return "Welcome " + args[0];
        }
        return null;
    }
}
