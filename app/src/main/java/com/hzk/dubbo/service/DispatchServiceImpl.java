package com.hzk.dubbo.service;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DispatchServiceImpl implements DispatchService {

    private Map<String, Method> serviceMethodMap = new ConcurrentHashMap();

    @Override
    public Object invoke(String serviceFactory, String serviceName, String methodName, Object... paras) {
        Object result = "";
        try {
            // 反射ServiceFactory
            Class<?> clazz = Class.forName(serviceFactory);
            Method getServiceMethod = clazz.getDeclaredMethod("getService", String.class);
            Object serviceImpl = getServiceMethod.invoke(null, serviceName);

            // 反射接口服务ServiceImpl
            Method method = findServiceMethod(serviceImpl.getClass(), methodName, paras.length, paras);
            result = method.invoke(serviceImpl, paras);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


//    private Method findServiceMethod(Class<?> clazz, String method, int paramterLength, Object[] args) {
//        String key = clazz.getName() + '#' + method + '#' + paramterLength;
//        Method serviceMethod = (Method)this.serviceMethodMap.get(key);
//
//        if (serviceMethod == null) {
//            Method[] var6 = clazz.getMethods();
//            int var7 = var6.length;
//            for(int var8 = 0; var8 < var7; ++var8) {
//                Method m = var6[var8];
//                if (m.getName().equalsIgnoreCase(method) && m.getParameterCount() == paramterLength) {
//                    serviceMethod = m;
//                    this.serviceMethodMap.putIfAbsent(key, m);
//                    break;
//                }
//            }
//            if (serviceMethod == null) {
//                throw new RuntimeException(String.format("未发现类%1$s的方法%2$s", clazz.getName(), method));
//            }
//        }
//        return serviceMethod;
//    }
    private Method findServiceMethod(Class<?> clazz, String method, int paramterLength, Object[] args) throws ClassNotFoundException, NoSuchMethodException {
        Class[] parameterClassArray = new Class[args.length];
        for (int i = 0; i < args.length; i++) {
            String tempParamType = args[i].getClass().getName();
            if (tempParamType.equals("int")) {
                parameterClassArray[i] = int.class;
            } else {
                parameterClassArray[i] = Class.forName(tempParamType);
            }
        }
        Method serviceMethod = clazz.getDeclaredMethod(method, parameterClassArray);
        return serviceMethod;
    }
}
