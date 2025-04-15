package com.hzk.nacos.v2;

import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.ConfigType;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;

/**
 * nacos配置中心客户端
 */
public class NacosConfigClient implements NacosConfigService {

    private String serverAddress;

    private ConfigService configService;

    public NacosConfigClient(String serverAddress, ConfigService configService){
        this.serverAddress = serverAddress;
        this.configService = configService;
    }

    private int getNacosRequestTimeout(){
        return Integer.getInteger("nacos.request.timeoutms", 10000);
    }

    @Override
    public String getConfig(String dataId, String group) throws NacosException {
        return configService.getConfig(dataId, group, getNacosRequestTimeout());
    }

    @Override
    public String getConfig(String dataId, String group, long timeoutMs) throws NacosException {
        return configService.getConfig(dataId, group, timeoutMs);
    }

    @Override
    public String getConfig(String dataId, String group, String tag, String appName, long timeoutMs) throws NacosException {
        return null;
    }

    @Override
    public String getConfigAndSignListener(String dataId, String group, Listener listener) throws NacosException {
        return configService.getConfigAndSignListener(dataId, group, getNacosRequestTimeout(), listener);
    }

    @Override
    public void addListener(String dataId, String group, Listener listener) throws NacosException {
        configService.addListener(dataId, group, listener);
    }

    @Override
    public boolean publishConfig(String dataId, String group, String content, ConfigType type) throws NacosException {
        return configService.publishConfig(dataId, group, content, type.toString());
    }


    @Override
    public boolean publishConfigCas(String dataId, String group, String content, String casMd5, ConfigType type) throws NacosException {
        return false;
//        return configService.publishConfigCas(dataId, group, content, casMd5, type.toString());
    }

    @Override
    public boolean removeConfig(String dataId, String group) throws NacosException {
        return configService.removeConfig(dataId, group);
    }

    @Override
    public void removeListener(String dataId, String group, Listener listener) {
        configService.removeListener(dataId, group, listener);
    }

    @Override
    public String getServerStatus() {
        return configService.getServerStatus();
    }

    @Override
    public void shutDown() throws NacosException {
        configService.shutDown();
        NacosFactory.removeConfigService(this.serverAddress);
    }
}
