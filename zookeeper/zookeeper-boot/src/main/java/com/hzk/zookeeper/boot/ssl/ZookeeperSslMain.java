package com.hzk.zookeeper.boot.ssl;

import com.hzk.zookeeper.factory.ZKFactory;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;

import java.util.Random;

/**
 * JVM参数
 * -DconnectString=172.20.158.201:2182?user=zookeeper&password=zookeeper -Dzookeeper.ssl.trustStore.location=D:\project\daydaystudy\zookeeper\zookeeper-boot\src\main\resources\zookeeper.truststore.jks -Dzookeeper.ssl.trustStore.password=trustPassword
 */
public class ZookeeperSslMain {

    static {
        System.setProperty("zookeeper.client.secure", "true");
        System.setProperty("zookeeper.clientCnxnSocket", "org.apache.zookeeper.ClientCnxnSocketNetty");
        System.setProperty("zookeeper.ssl.protocol", "TLSv1.2");
        System.setProperty("jdk.tls.rejectClientInitiatedRenegotiation", System.getProperty("jdk.tls.rejectClientInitiatedRenegotiation", "true"));//NOSONAR
    }


    public static void main(String[] args) throws Exception {
        for (int i = 0; i < args.length; i++) {
            String[] tempArr = args[i].split("=");
            System.setProperty(tempArr[0], tempArr[1]);
        }
        String connectString = System.getProperty("connectString");
        CuratorFramework zkClient = ZKFactory.getZKClient(connectString);
        System.err.println("---zkClient start---");
        // create
        Random random = new Random();
        String path = "ssl";
        String node = "node" + random.nextInt(100);
        String zkPathNode = "/" + path + "/" + node;
        // 递归创建节点
        zkClient.create()
                .creatingParentsIfNeeded()
                .withMode(CreateMode.PERSISTENT)
                .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                .forPath(zkPathNode,("ssltest").getBytes());
        System.err.println("create success,path:" + zkPathNode);
        // get
        byte[] bytes = zkClient.getData().forPath(zkPathNode);
        String value = new String(bytes);
        System.err.println("get success,path:" + zkPathNode + ",value:" + value);
        // delete
        zkClient.delete().forPath(zkPathNode);
        System.err.println("delete success,path:" + zkPathNode);

        System.err.println("---zkClient close---");
        zkClient.close();
    }

}
