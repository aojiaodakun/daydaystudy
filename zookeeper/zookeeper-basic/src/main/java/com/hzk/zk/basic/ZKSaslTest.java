package com.hzk.zk.basic;

import com.hzk.zk.constants.BasicConstants;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

public class ZKSaslTest {

    ZooKeeper zooKeeper = null;

    @Before
    public void before(){
        try{
            System.setProperty("java.security.auth.login.config", "D:\\project\\daydaystudy\\zookeeper\\src\\main\\resources\\jaas.conf");
            String url = "172.20.158.201:2181";
            //创建一个计数器对象
            CountDownLatch countDownLatch = new CountDownLatch(1);
            //第一个参数是服务器ip和端口号，第二个参数是客户端与服务器的会话超时时间单位ms，第三个参数是监视器对象
            zooKeeper = new ZooKeeper(url, 5000, new Watcher() {
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
        zooKeeper.create("/node1", ("value1").getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);

        Stat stat = new Stat();
        byte[] data = zooKeeper.getData("/node1", false, stat);
        System.out.println(new String(data));
    }

}
