package com.hzk.zk.basic.watcher;

import com.hzk.zk.constants.BasicConstants;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

public class ZKGetDataWatcher {

    ZooKeeper zooKeeper = null;

    @Before
    public void before(){
        try{
            //创建一个计数器对象
            CountDownLatch countDownLatch = new CountDownLatch(1);
            //第一个参数是服务器ip和端口号，第二个参数是客户端与服务器的会话超时时间单位ms，第三个参数是监视器对象
            zooKeeper = new ZooKeeper(BasicConstants.IP_ALONE, 5000, new Watcher() {
                @Override
                public void process(WatchedEvent event) {
                    if(event.getState()== Watcher.Event.KeeperState.SyncConnected){
                        System.out.println("连接创建成功");
                        //通知主线程解除阻塞
                        countDownLatch.countDown();
                    }
                    System.out.println("path=" + event.getPath());
                    System.out.println("eventType=" + event.getType());
                }
            });
            //主线程阻塞，等待连接对象的创建成功
            countDownLatch.await();
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


    @Test
    public void get1() throws Exception{
        Stat stat = new Stat();
        byte[] data = zooKeeper.getData("/watcher1", true, stat);
        System.out.println(new String(data));

        Thread.sleep(1000 *50);
        System.out.println("结束");
    }


    @Test
    public void get2() throws Exception{
        Stat stat = new Stat();
        byte[] data = zooKeeper.getData("/watcher1", (e)->{
            System.out.println("自定义watcher");
            System.out.println("path=" + e.getPath());
            System.out.println("eventType=" + e.getType());
        }, stat);
        System.out.println(new String(data));

        Thread.sleep(1000 *50);
        System.out.println("结束");
    }


    @Test
    public void get3() throws Exception{
        Stat stat = new Stat();
        Watcher watcher = new Watcher() {
            @Override
            public void process(WatchedEvent e) {
                System.out.println("自定义watcher");
                System.out.println("path=" + e.getPath());
                System.out.println("eventType=" + e.getType());
            }
        };
        byte[] data = zooKeeper.getData("/watcher1", watcher, stat);
        System.out.println(new String(data));

        Thread.sleep(1000 *50);
        System.out.println("结束");
    }

    @Test
    public void get4() throws Exception{
        Stat stat = new Stat();
        Watcher watcher = new Watcher() {
            @Override
            public void process(WatchedEvent e) {
                System.out.println("自定义watcher");
                System.out.println("path=" + e.getPath());
                System.out.println("eventType=" + e.getType());
            }
        };
        byte[] data = zooKeeper.getData("/watcher1", watcher, stat);
        System.out.println(new String(data));


        Watcher watcher1 = new Watcher() {
            @Override
            public void process(WatchedEvent e) {
                System.out.println("自定义watcher");
                System.out.println("path=" + e.getPath());
                System.out.println("eventType=" + e.getType());
            }
        };
        byte[] data1 = zooKeeper.getData("/watcher1", watcher1, stat);
        System.out.println(new String(data1));

        Thread.sleep(1000 *50);
        System.out.println("结束");
    }


    @Test
    public void get5() throws Exception{
        Stat stat = new Stat();
        Watcher watcher = new Watcher() {
            @Override
            public void process(WatchedEvent e) {
                System.out.println("自定义watcher");
                System.out.println("path=" + e.getPath());
                System.out.println("eventType=" + e.getType());
                try {
                    zooKeeper.getData("/watcher1",this,stat);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };

        byte[] data = zooKeeper.getData("/watcher1", watcher, stat);
        System.out.println(new String(data));

        Thread.sleep(1000 *50);
        System.out.println("结束");
    }

}
