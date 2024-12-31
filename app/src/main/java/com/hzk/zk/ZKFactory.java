package com.hzk.zk;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.concurrent.ConcurrentHashMap;

public class ZKFactory {

    public static final ConcurrentHashMap<String, CuratorFramework> poolMap = new ConcurrentHashMap();

    public static CuratorFramework getZkClient(String url) {
        if (poolMap.containsKey(url)) {
            return poolMap.get(url);
        }
        synchronized (ZKFactory.class) {
            if (poolMap.containsKey(url)) {
                return poolMap.get(url);
            }
            RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000,3);
            CuratorFramework client = CuratorFrameworkFactory.builder()
                    .connectString(url)
                    .sessionTimeoutMs(1000 * 20)
                    .retryPolicy(retryPolicy)
                    .build();
            client.start();
            poolMap.put(url, client);
        }
        return poolMap.get(url);
    }


}
