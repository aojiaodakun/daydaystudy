package com.hzk.service;

import java.lang.reflect.Method;

public class DispatchServiceImpl implements DispatchService{
    @Override
    public String invoke(String className, String methodName, String[] parameterTypes, Object[] args) {
        String result = "";
        try {
            Class<?> clazz = Class.forName(className);
            Class[] parameterClassArray = new Class[parameterTypes.length];
            for (int i = 0; i < parameterTypes.length; i++) {
                String tempParamType = parameterTypes[i];
                if (tempParamType.equals("int")) {
                    parameterClassArray[i] = int.class;
                } else {
                    parameterClassArray[i] = Class.forName(parameterTypes[i]);
                }
            }
            Method method = clazz.getDeclaredMethod(methodName, parameterClassArray);
            Object object = clazz.newInstance();
            Object invoke = method.invoke(object, args);
            result = invoke.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
