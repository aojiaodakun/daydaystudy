package com.hzk.zk.basic.config;

import com.hzk.zk.constants.BasicConstants;
import com.hzk.zk.basic.watcher.ZKConnWatcher;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.util.concurrent.CountDownLatch;

/**
 * 配置中心demo
 */
public class MyConfigCenter implements Watcher {

    //创建一个计数器对象
    CountDownLatch countDownLatch = new CountDownLatch(1);

    static ZooKeeper zooKeeper = null;

    // jdbc数据
    String url;
    String username;
    String password;

    public MyConfigCenter() throws Exception{
        initValue();
    }

    @Override
    public void process(WatchedEvent event) {
        try {
            // 事件类型
            if(event.getType() == Event.EventType.None){
                // 事件状态
                if(event.getState() == Event.KeeperState.SyncConnected){
                    System.out.println("连接创建成功");
                    //通知主线程解除阻塞
                    countDownLatch.countDown();
                }else if(event.getState() == Event.KeeperState.Disconnected){
                    System.out.println("断开连接");
                }else if(event.getState() == Event.KeeperState.Expired){
                    System.out.println("会话超时");
                    zooKeeper = new ZooKeeper(BasicConstants.IP_ALONE, 5000, new ZKConnWatcher());
                }else if(event.getState() == Event.KeeperState.AuthFailed){
                    System.out.println("认证失败");
                }
            }else if(event.getType() == Event.EventType.NodeDataChanged){
                initValue();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public void initValue() throws Exception{
        // 超时时间问题记录：sessionTimeout。修改节点数据、重连、获取数据耗时长，需把创建连接时间加长
        zooKeeper = new ZooKeeper(BasicConstants.IP_ALONE, 1000 * 20, this);
        countDownLatch.await();
        this.url = new String(zooKeeper.getData("/config/url",true,null));
        this.username = new String(zooKeeper.getData("/config/username",true,null));
        this.password = new String(zooKeeper.getData("/config/password",true,null));
    }


    public static void main(String[] args) throws Exception {
        MyConfigCenter configCenter = new MyConfigCenter();
        for (int i = 0; i < 20; i++) {
            Thread.sleep(3000);
            System.out.println("url:" + configCenter.getUrl());
            System.out.println("username:" + configCenter.getUsername());
            System.out.println("password:" + configCenter.getPassword());
            System.out.println("#############################");
        }
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
