package com.hzk.zk.basic.uniqueid;

import com.hzk.zk.constants.BasicConstants;
import com.hzk.zk.basic.watcher.ZKConnWatcher;
import org.apache.zookeeper.*;

import java.util.concurrent.CountDownLatch;

public class GloballyUniqueIdDemo implements Watcher {

    //创建一个计数器对象
    CountDownLatch countDownLatch = new CountDownLatch(1);

    String defaultPath = "/uniqueid";

    static ZooKeeper zooKeeper = null;


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
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public GloballyUniqueIdDemo(){
        try {
            zooKeeper = new ZooKeeper(BasicConstants.IP_ALONE, 1000 * 20, this);
            countDownLatch.await();
        }catch (Exception e){

        }
    }

    /**
     * 生成唯一id
     * @return
     */
    public String getUniqueId(){
        String path = "";
        try {
            path = zooKeeper.create(defaultPath,new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        }catch (Exception e){
            e.printStackTrace();
        }
        return path.substring(9);
    }

    public static void main(String[] args) {
        GloballyUniqueIdDemo globallyUniqueIdDemo = new GloballyUniqueIdDemo();
        for (int i = 0; i < 10; i++) {
            System.out.println(globallyUniqueIdDemo.getUniqueId());
        }
    }

}
