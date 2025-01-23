package com.hzk.dubbo.service;

public interface DispatchService {

    Object invoke(String serviceFactory, String serviceName, String methodName, Object... paras);

}
