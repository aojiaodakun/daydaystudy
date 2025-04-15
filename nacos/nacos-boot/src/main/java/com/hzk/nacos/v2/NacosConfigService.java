package com.hzk.nacos.v2;

import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.ConfigType;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;

public interface NacosConfigService extends ConfigService {

    /**
     * Get config.
     *
     * @param dataId    dataId
     * @param group     group
     * @return config value
     * @throws NacosException NacosException
     */
    String getConfig(String dataId, String group) throws NacosException;


    /**
     * Get config and register Listener.
     *
     * <p>If you want to pull it yourself when the program starts to get the configuration for the first time, and the
     * registered Listener is used for future configuration updates, you can keep the original code unchanged, just add
     * the system parameter: enableRemoteSyncConfig = "true" ( But there is network overhead); therefore we recommend
     * that you use this interface directly
     *
     * @param dataId    dataId
     * @param group     group
     * @param listener  {@link Listener}
     * @return config value
     * @throws NacosException NacosException
     */
    String getConfigAndSignListener(String dataId, String group, Listener listener)
            throws NacosException;

    @Override
    default String getConfigAndSignListener(String dataId, String group, long timeoutMs, Listener listener) {
        return null;
    }

    /**
     * Publish config.
     *
     * @param dataId  dataId
     * @param group   group
     * @param content content
     * @param type    config type {@link ConfigType}
     * @return Whether publish
     * @throws NacosException NacosException
     */
    boolean publishConfig(String dataId, String group, String content, ConfigType type) throws NacosException;

    /**
     * Publish config.
     *
     * @param dataId  dataId
     * @param group   group
     * @param content content
     * @return Whether publish
     */
    @Override
    default boolean publishConfig(String dataId, String group, String content) {
        return false;
    }

    /**
     * Publish config.
     *
     * @param dataId  dataId
     * @param group   group
     * @param content content
     * @param type    config type {@link ConfigType}
     * @return Whether publish
     */
    default boolean publishConfig(String dataId, String group, String content, String type) {
        return false;
    }
    /**
     * Cas Publish config.
     *
     * @param dataId  dataId
     * @param group   group
     * @param content content
     * @param casMd5  casMd5 prev content's md5 to cas.
     * @param type    config type {@link ConfigType}
     * @return Whether publish
     * @throws NacosException NacosException
     */
    boolean publishConfigCas(String dataId, String group, String content, String casMd5, ConfigType type)
            throws NacosException;
    /**
     * Cas Publish config.
     *
     * @param dataId  dataId
     * @param group   group
     * @param content content
     * @param casMd5  casMd5 prev content's md5 to cas.
     * @return Whether publish
     */
    default boolean publishConfigCas(String dataId, String group, String content, String casMd5) {
        return false;
    }

    /**
     * Cas Publish config.
     *
     * @param dataId  dataId
     * @param group   group
     * @param content content
     * @param casMd5  casMd5 prev content's md5 to cas.
     * @param type    config type {@link ConfigType}
     * @return Whether publish
     */
    default boolean publishConfigCas(String dataId, String group, String content, String casMd5, String type){
        return false;
    }

}

