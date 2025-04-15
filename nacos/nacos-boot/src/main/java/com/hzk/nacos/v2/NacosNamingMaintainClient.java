package com.hzk.nacos.v2;


import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingMaintainService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.api.naming.pojo.Service;
import com.alibaba.nacos.api.selector.AbstractSelector;

import java.util.Map;

public class NacosNamingMaintainClient implements NamingMaintainService {

    private String serverAddress;

    private NamingMaintainService namingMaintainService;

    public NacosNamingMaintainClient(String serverAddress, NamingMaintainService namingMaintainService){
        this.serverAddress = serverAddress;
        this.namingMaintainService = namingMaintainService;
    }

    @Override
    public void updateInstance(String serviceName, Instance instance) throws NacosException {
        namingMaintainService.updateInstance(serviceName, instance);
    }

    @Override
    public void updateInstance(String serviceName, String groupName, Instance instance) throws NacosException {
        namingMaintainService.updateInstance(serviceName, groupName, instance);
    }

    @Override
    public Service queryService(String serviceName) throws NacosException {
        return namingMaintainService.queryService(serviceName);
    }

    @Override
    public Service queryService(String serviceName, String groupName) throws NacosException {
        return namingMaintainService.queryService(serviceName, groupName);
    }

    @Override
    public void createService(String serviceName) throws NacosException {
        namingMaintainService.createService(serviceName);
    }

    @Override
    public void createService(String serviceName, String groupName) throws NacosException {
        namingMaintainService.createService(serviceName, groupName);
    }

    @Override
    public void createService(String serviceName, String groupName, float protectThreshold) throws NacosException {
        namingMaintainService.createService(serviceName, groupName, protectThreshold);
    }

    @Override
    public void createService(String serviceName, String groupName, float protectThreshold, String expression) throws NacosException {
        namingMaintainService.createService(serviceName, groupName, protectThreshold, expression);
    }

    @Override
    public void createService(Service service, AbstractSelector selector) throws NacosException {
        namingMaintainService.createService(service, selector);
    }

    @Override
    public boolean deleteService(String serviceName) throws NacosException {
        return namingMaintainService.deleteService(serviceName);
    }

    @Override
    public boolean deleteService(String serviceName, String groupName) throws NacosException {
        return namingMaintainService.deleteService(serviceName, groupName);
    }

    @Override
    public void updateService(String serviceName, String groupName, float protectThreshold) throws NacosException {
        namingMaintainService.updateService(serviceName, groupName, protectThreshold);
    }

    @Override
    public void updateService(String serviceName, String groupName, float protectThreshold, Map<String, String> metadata) throws NacosException {
        namingMaintainService.updateService(serviceName, groupName, protectThreshold, metadata);
    }

    @Override
    public void updateService(Service service, AbstractSelector selector) throws NacosException {
        namingMaintainService.updateService(service, selector);
    }

    @Override
    public void shutDown() throws NacosException {
        namingMaintainService.shutDown();
        NacosFactory.removeNamingMaintainService(this.serverAddress);
    }
}
