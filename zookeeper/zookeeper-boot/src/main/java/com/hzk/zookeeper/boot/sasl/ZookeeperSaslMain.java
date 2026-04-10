package com.hzk.zookeeper.boot.sasl;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.AuthInfo;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.ACLProvider;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 1、验证ACL
 * clusterName=ierp scheme=digest connectString=127.0.0.1:2181 user=zookeeper password=zookeeper
 * 2、验证SASL，D:\tool\zookeeper-3.6.1\bin
 * clusterName=ierp scheme=sasl connectString=127.0.0.1:2181 jaasConfPath=/home/jaas.conf
 * 3、招银：验证客户自定义SASL
 * clusterName=ierp scheme=curatorsasl connectString=127.0.0.1:2181 user=zookeeper password=zookeeper
 */
public class ZookeeperSaslMain {

    public static void main(String[] args) throws Exception {
        for (int i = 0; i < args.length; i++) {
            String[] tempArr = args[i].split("=");
            System.setProperty(tempArr[0], tempArr[1]);
        }
        String scheme = System.getProperty("scheme");
        String connectString = System.getProperty("connectString");

        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000,3);
        CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder()
                .connectString(connectString)
                .sessionTimeoutMs(1000 * 20)
                .retryPolicy(retryPolicy);
        CuratorFramework client = null;
        if (scheme.equals("digest")) {
            System.out.println("digest start");
            client = digestTest(builder);
        } else if(scheme.equals("sasl")) {
            System.out.println("sasl start");
            client = saslTest(builder);
        } else if(scheme.equals("curatorsasl")) {
            System.out.println("curatorsasl start");
            client = curatorSaslTest(builder);
        }
        System.out.println("client started");
//        // check
//        Stat stat = client.checkExists().forPath("/mc");
//        if (stat != null) {
//            System.out.println("mc node version:" + stat.getVersion());
//        }
        // create
        Random random = new Random();
        String path = "sasl";
        String node = "node" + random.nextInt(100);
        String zkPathNode = "/" + path + "/" + node;
        // 递归创建节点
        client.create()
                .creatingParentsIfNeeded()
                .withMode(CreateMode.PERSISTENT)
                .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                .forPath(zkPathNode,("sasltest").getBytes());
        System.out.println("create success,path:" + zkPathNode);
        // get
        byte[] bytes = client.getData().forPath(zkPathNode);
        String value = new String(bytes);
        System.out.println("get success,path:" + zkPathNode + ",value:" + value);
        // delete
        client.delete().forPath(zkPathNode);
        System.out.println("delete success,path:" + zkPathNode);

        client.close();
        System.out.println("client close success");
    }

    private static CuratorFramework digestTest(CuratorFrameworkFactory.Builder builder) {
        String user = System.getProperty("user");
        String password = System.getProperty("password");
        String auth = user + ":" + password;
        ACLProvider aclProvider = new ACLProvider() {
            private List<ACL> acl;
            @Override
            public List<ACL> getDefaultAcl() {
                if (acl == null) {
                    ArrayList<ACL> acl = ZooDefs.Ids.CREATOR_ALL_ACL; // 初始化
                    acl.clear();
                    acl.add(new ACL(ZooDefs.Perms.ALL, new Id("auth", auth)));// 添加
                    this.acl = acl;
                }
                return acl;
            }

            @Override
            public List<ACL> getAclForPath(String path) {
                return acl;
            }
        };
        builder.authorization("digest", (auth).getBytes());
        builder.aclProvider(aclProvider);
        CuratorFramework client = builder.build();
        client.start();
        return client;
    }

    private static CuratorFramework saslTest(CuratorFrameworkFactory.Builder builder) {
        String jaasConfPath = System.getProperty("jaasConfPath");
        if (jaasConfPath == null || jaasConfPath.length() == 0) {
            jaasConfPath = ZookeeperSaslMain.class.getResource("/jaas.conf").getPath();
        }
        System.out.println("jaasConfPath:" + jaasConfPath);
        System.setProperty("java.security.auth.login.config", jaasConfPath);
        CuratorFramework client = builder.build();
        client.start();
        return client;
    }

    /**
     * 客户改造了zk-server，类似digest的认证
     */
    private static CuratorFramework curatorSaslTest(CuratorFrameworkFactory.Builder builder) {
        String user = System.getProperty("user");
        String password = System.getProperty("password");
        String auth = user + ":" + password;
        List<AuthInfo> authInfoList = new ArrayList<>();
        authInfoList.add(new AuthInfo("sasl", (auth).getBytes()));
        builder.authorization(authInfoList);
        CuratorFramework client = builder.build();
        client.start();
        return client;
    }

}
