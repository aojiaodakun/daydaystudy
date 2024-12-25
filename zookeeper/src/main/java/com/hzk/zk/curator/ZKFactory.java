package com.hzk.zk.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ZKFactory {

    private static Map<String, CuratorFramework> URL_CLIENT_MAP = new ConcurrentHashMap<>();

    public static CuratorFramework getZKClient(String url){
        if (URL_CLIENT_MAP.containsKey(url)) {
            return URL_CLIENT_MAP.get(url);
        }
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000,3);
        CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder()
                .connectString(url)
                .sessionTimeoutMs(1000 * 20)
                .retryPolicy(retryPolicy);
        CuratorFramework client = builder.build();
        client.start();
        URL_CLIENT_MAP.put(url, client);
        return client;
    }

}
