package com.hzk.service;

import java.util.HashMap;
import java.util.Map;

public class ServiceFactory {
    private static Map<String, String> serviceMap = new HashMap<>();
    private static final ThreadLocal<Map<String, Object>> serviceInstanceMap = new ThreadLocal();

    public ServiceFactory() {
    }

    public static void putService(String serviceName, String serviceImpl) {
        serviceMap.put(serviceName, serviceImpl);
    }

    public static Object getService(String serviceName) {
        String className = serviceMap.get(serviceName);
        if (className == null) {
            throw new RuntimeException("对应的服务实现类未找到：" + serviceName);
        } else {
            Object obj = null;
            try {
                obj = Class.forName(className).newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return obj;
        }
    }

    static {
        serviceMap.put("dubboMService", "com.hzk.dubbo.mservice.DubboMService");
    }
}
