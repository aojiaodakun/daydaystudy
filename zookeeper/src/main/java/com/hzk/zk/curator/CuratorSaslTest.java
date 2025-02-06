package com.hzk.zk.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * https://developer.aliyun.com/article/1115582
 * 开启SASL后，会覆盖ACL的权限
 */
public class CuratorSaslTest {

    CuratorFramework client;

    @Before
    public void before() {
        String jaasConfPath = CuratorSaslTest.class.getResource("/jaas.conf").getPath();
        System.setProperty("java.security.auth.login.config", jaasConfPath);
        String url = "127.0.0.1:2181";

        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder()
                .connectString(url)
                .sessionTimeoutMs(1000 * 2000)
                .retryPolicy(retryPolicy);
        client = CuratorFrameworkFactory.builder()
                .connectString(url)
                .sessionTimeoutMs(1000 * 2000)
                .retryPolicy(retryPolicy)
                .build();
        client.start();
    }


    @Test
    public void saslTest() throws Exception {
        String node = "/test6";
        // create
        client.create()
                .creatingParentsIfNeeded()
                .withMode(CreateMode.PERSISTENT)
//                .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                .withACL(ZooDefs.Ids.CREATOR_ALL_ACL)
                .forPath(node, ("sasl-value").getBytes());
        System.out.println("create success");
        // getAcl
        List<ACL> list = client.getACL().forPath(node);
        // setData
        client.setData().forPath(node, ("value4").getBytes());
        System.out.println("set success");
        // getData
        byte[] bytes = client.getData().forPath(node);
        String value = new String(bytes);
        System.out.println(value);
        System.out.println("get success");
    }


}
