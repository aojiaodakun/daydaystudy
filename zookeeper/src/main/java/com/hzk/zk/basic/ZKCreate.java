package com.hzk.zk.basic;

import com.hzk.zk.constants.BasicConstants;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class ZKCreate {

    String context = "I am context";
    ZooKeeper zooKeeper = null;

    @Before
    public void before(){
        try{
            //创建一个计数器对象
            CountDownLatch countDownLatch = new CountDownLatch(1);
            //第一个参数是服务器ip和端口号，第二个参数是客户端与服务器的会话超时时间单位ms，第三个参数是监视器对象
            zooKeeper = new ZooKeeper(BasicConstants.IP, 5000, new Watcher() {
                @Override
                public void process(WatchedEvent event) {
                    if(event.getState()== Watcher.Event.KeeperState.SyncConnected){
                        System.out.println("连接创建成功");
                        //通知主线程解除阻塞
                        countDownLatch.countDown();
                    }
                }
            });
            //主线程阻塞，等待连接对象的创建成功
            countDownLatch.await();
//            System.out.println("会话编号"+zooKeeper.getSessionId());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @After
    public void after(){
        if(zooKeeper!=null) {
            try {
                zooKeeper.close();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // arg1:节点路径
    // agr2:节点数据
    // arg3:节点权限
    // arg4:节点类型
    @Test
    public void create1() throws Exception{
        String node = "1";
        zooKeeper.create("/create2" + node,("node" + node).getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
    }

    @Test
    public void create2() throws Exception{
        String node = "2";
        zooKeeper.create("/create",("node" + node).getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
    }

    @Test
    public void create3() throws Exception{
        String node = "3";
        // 权限列表
        List<ACL> acls = new ArrayList<>();
        // 授权模式和授权对象
        Id id = new Id("world","anyone");
        acls.add(new ACL(ZooDefs.Perms.READ,id));
        acls.add(new ACL(ZooDefs.Perms.WRITE,id));
        zooKeeper.create("/create/node" + node,("node" + node).getBytes(), acls, CreateMode.PERSISTENT);
    }

    @Test
    public void create4() throws Exception{
        String node = "4";
        // 权限列表
        List<ACL> acls = new ArrayList<>();
        // 授权模式和授权对象
        Id id = new Id("ip",BasicConstants.IP);
        acls.add(new ACL(ZooDefs.Perms.ALL,id));
        zooKeeper.create("/create/node" + node,("node" + node).getBytes(), acls, CreateMode.PERSISTENT);
    }

    @Test
    public void create5() throws Exception{
        String node = "5";
        // auth授权模式
        zooKeeper.addAuthInfo("digest","hzk:123456".getBytes());
        zooKeeper.create("/create/node" + node,("node" + node).getBytes(), ZooDefs.Ids.CREATOR_ALL_ACL, CreateMode.PERSISTENT);
    }

    @Test
    public void create6() throws Exception{
        String node = "6";
        // auth授权模式
        zooKeeper.addAuthInfo("digest","hzk:123456".getBytes());
        List<ACL> acls = new ArrayList<>();
        Id id = new Id("auth","hzk");
        acls.add(new ACL(ZooDefs.Perms.READ,id));
        zooKeeper.create("/create/node" + node,("node" + node).getBytes(), acls, CreateMode.PERSISTENT);
    }


    @Test
    public void create7() throws Exception{
        String node = "7";
        // auth授权模式
        List<ACL> acls = new ArrayList<>();
        Id id = new Id("digest","itheima:qlzQzCLKhBROghkooLvb+Mlwv4A=");
        acls.add(new ACL(ZooDefs.Perms.ALL,id));
        zooKeeper.create("/create/node" + node,("node" + node).getBytes(), acls, CreateMode.PERSISTENT);
    }

    @Test
    public void create8() throws Exception{
        String node = "8";
        String result = zooKeeper.create("/create/node" + node,("node" + node).getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT_SEQUENTIAL);
    }

    @Test
    public void create9() throws Exception{
        String node = "9";
        String result = zooKeeper.create("/create/node" + node,("node" + node).getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        System.out.println();
    }

    @Test
    public void create10() throws Exception{
        String node = "10";
        String result = zooKeeper.create("/create/node" + node,("node" + node).getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
    }

    @Test
    public void create11() throws Exception{
        String node = "11";
        zooKeeper.create("/create/node" + node, ("node" + node).getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT, new AsyncCallback.StringCallback() {
            @Override
            public void processResult(int rc, String path, Object ctx, String name) {
                // 0 代表创建成功
                System.out.println(rc);
                // 节点的路径
                System.out.println(path);
                // 上下文参数
                System.out.println(ctx);
                // 节点的路径
                System.out.println(name);
            }
        },context);
        Thread.sleep(1000 * 10);
        System.out.println("结束");
    }


}
