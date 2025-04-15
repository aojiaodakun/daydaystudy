package com.hzk.zk.curator;

import com.hzk.zk.constants.BasicConstants;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.ACLProvider;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class CuratorAcl {

    CuratorFramework client;

    String namespace = "create";

    @Before
    public void before(){
        String url = "172.20.187.96:2182,172.20.187.97:2182,172.20.187.98:2182?user=zookeeper&password=d@f*g:SGVsbG8==YRcksHuOclqFzAifFiaruDTO91DxjvINf52SWdGdd8Fsa2RwYXNzd29yZA==";
        url = "172.20.187.96:2182,172.20.187.97:2182,172.20.187.98:2182?user=zookeeper&password=111";


        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000,3);
        CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder()
//                .connectString(BasicConstants.IP)
                .connectString(url)
                .sessionTimeoutMs(1000 * 20)
                .retryPolicy(retryPolicy);

        String user = "zookeeper";
        String pass = "123456";

        if (user != null && pass != null) {
            String auth = user + ":" + pass;
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
        }
        client = builder.build();
        client.start();
    }

    @After
    public void after(){
        client.close();
    }


    @Test
    public void create() throws Exception{
        String node = "1";
        client.create()
                .withMode(CreateMode.PERSISTENT)
//                .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                .withACL(ZooDefs.Ids.CREATOR_ALL_ACL)
                .forPath("/node" + node,("node" + node).getBytes());
        System.out.println("结束");
    }


    @Test
    public void delete1() throws Exception{
        String node = "1";
        client.delete()
                .forPath("/node" + node);
        System.out.println("结束");
    }

    @Test
    public void get1() throws Exception{
        String node = "1";
        byte[] bytes = client.getData()
                .forPath("/node" + node);
        System.out.println(new String(bytes));
    }

}
