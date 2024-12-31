package com.hzk.zk.curator;

import com.hzk.zk.constants.BasicConstants;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CuratorDelete {

    CuratorFramework client;

    String namespace = "create";

    @Before
    public void before(){
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000,3);
        client = CuratorFrameworkFactory.builder()
                .connectString(BasicConstants.IP)
                .sessionTimeoutMs(1000 * 20)
                .retryPolicy(retryPolicy)
                .namespace(namespace)
                .build();
        client.start();
    }

    @After
    public void after(){
        client.close();
    }


    @Test
    public void delete1() throws Exception{
        String node = "2";
        client.delete()
                .forPath("/node" + node);
        System.out.println("结束");
    }

    @Test
    public void delete2() throws Exception{
        String node = "2";
        client.delete()
                .withVersion(-1)
                .forPath("/node" + node);
        System.out.println("结束");
    }

    @Test
    public void delete3() throws Exception{
        String node = "3";
        client.delete()
                .deletingChildrenIfNeeded()
                .withVersion(-1)
                .forPath("node1");
        System.out.println("结束");
    }

    @Test
    public void delete4() throws Exception{
        String node = "4";
        client.delete()
                .withVersion(-1)
                .inBackground(new BackgroundCallback() {
                    @Override
                    public void processResult(CuratorFramework curatorFramework, CuratorEvent curatorEvent) throws Exception {
                        System.out.println(curatorEvent.getPath());
                        System.out.println(curatorEvent.getType());
                    }
                })
                .forPath("/node" + node);
        Thread.sleep(1000 * 5);
        System.out.println("结束");
    }

}
