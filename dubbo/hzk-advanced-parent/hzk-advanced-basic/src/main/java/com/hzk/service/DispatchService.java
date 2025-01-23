package com.hzk.service;

public interface DispatchService {

    String invoke(String className, String methodName, String[] parameterTypes, Object[] args);

}
