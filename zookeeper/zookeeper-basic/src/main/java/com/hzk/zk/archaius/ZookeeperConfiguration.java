package com.hzk.zk.archaius;

import java.util.Iterator;

import com.hzk.zk.constants.BasicConstants;
import com.netflix.config.DynamicWatchedConfiguration;


import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class ZookeeperConfiguration {
    private static CuratorFramework client;
    private ZooKeeperConfigurationSource zkConfigSource;
    private DynamicWatchedConfiguration zkConfiguration;

    public ZookeeperConfiguration(String url, String rootPath) {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000,3);
        client = CuratorFrameworkFactory.builder()
                .connectString(BasicConstants.IP)
                .sessionTimeoutMs(1000 * 20)
                .retryPolicy(retryPolicy)
                .build();
        client.start();
        zkConfigSource = new ZooKeeperConfigurationSource(client, rootPath);
        try {
            zkConfigSource.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            zkConfiguration = new DynamicWatchedConfiguration(zkConfigSource);
            zkConfiguration.setDelimiterParsingDisabled(true);
        }catch(Exception t){
            t.printStackTrace();
        }

    }

    public DynamicWatchedConfiguration getZKConfiguration()
    {
        return zkConfiguration;
    }

    @SuppressWarnings("unchecked")
    public Iterator<String> keys() {
        return zkConfiguration.getKeys();
    }

    public String getProperty(String key) {
        return getProperty(key, null);
    }

    public String getProperty(String key, String defaultValue) {
        String value = zkConfiguration.getString(key);
        return value==null?defaultValue:value;
    }

}