package com.hzk.zk.curator;

import com.hzk.zk.constants.BasicConstants;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.client.ConnectStringParser;
import org.apache.zookeeper.client.HostProvider;
import org.apache.zookeeper.client.StaticHostProvider;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CuratorCreate {

    CuratorFramework client;

    String namespace = "curator";

    @Before
    public void before(){
//        String url = "127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183";
        String url = "127.0.0.1:2181";
//        ConnectStringParser connectStringParser = new ConnectStringParser(url);
//        HostProvider hostProvider = new StaticHostProvider(connectStringParser.getServerAddresses());
//        ArrayList<InetSocketAddress> serverAddresses = connectStringParser.getServerAddresses();
//        for(InetSocketAddress inetSocketAddress : serverAddresses) {
//            System.out.println(inetSocketAddress);
//        }
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000,3);
        client = CuratorFrameworkFactory.builder()
                    .connectString(url)
                    .sessionTimeoutMs(1000 * 2000)
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
    public void create1() throws Exception{
        String node = "/type";
        byte[] bytes = node.getBytes("utf-8");
        client.create()
                .creatingParentsIfNeeded()
                .withMode(CreateMode.PERSISTENT)
                .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                .forPath("/webserver" + node,("jetty").getBytes());
        System.out.println("结束");
    }


    @Test
    public void create2() throws Exception{
        String node = "2";
        List<ACL> list = new ArrayList<>();
        Id id = new Id("ip", BasicConstants.IP);
        list.add(new ACL(ZooDefs.Perms.ALL,id));
        client.create()
                .withMode(CreateMode.PERSISTENT)
                .withACL(list)
                .forPath("/node" + node,("node" + node).getBytes());
        System.out.println("结束");
    }

    @Test
    public void create3() throws Exception{
        String node = "3";
        // 递归创建节点
        client.create()
                .creatingParentsIfNeeded()
                .withMode(CreateMode.PERSISTENT)
                .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                .forPath("/node3/node" + node,("node" + node).getBytes());
        System.out.println("结束");
    }

    // 异步
    @Test
    public void create4() throws Exception{
        String node = "4";
        // 递归创建节点
        client.create()
                .creatingParentsIfNeeded()
                .withMode(CreateMode.PERSISTENT)
                .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                .inBackground(new BackgroundCallback() {
                    @Override
                    public void processResult(CuratorFramework curatorFramework, CuratorEvent curatorEvent) throws Exception {
                        System.out.println("2");
                        System.out.println(curatorEvent.getPath());
                        System.out.println(curatorEvent.getType());
                    }
                })
                .forPath("/node" + node,("node" + node).getBytes());
        System.out.println("1");
        Thread.sleep(1000 * 5);
        System.out.println("结束");
    }


}
