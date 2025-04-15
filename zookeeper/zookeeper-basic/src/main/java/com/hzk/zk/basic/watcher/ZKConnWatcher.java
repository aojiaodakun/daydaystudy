package com.hzk.zk.basic.watcher;

import com.hzk.zk.constants.BasicConstants;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

public class ZKConnWatcher implements Watcher {

    static ZooKeeper zooKeeper = null;

    //创建一个计数器对象
    static CountDownLatch countDownLatch=new CountDownLatch(1);

    @Override
    public void process(WatchedEvent event)  {
        try {
            // 事件类型
            if(event.getType().equals(Event.EventType.None)){
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



    @Test
    public void connect(){
        try{
            //第一个参数是服务器ip和端口号，第二个参数是客户端与服务器的会话超时时间单位ms，第三个参数是监视器对象
            zooKeeper = new ZooKeeper(BasicConstants.IP_ALONE, 5000, new ZKConnWatcher());
            //主线程阻塞，等待连接对象的创建成功
            countDownLatch.await();
            System.out.println("会话编号:"+zooKeeper.getSessionId());
            // 添加授权用户
            // create /node1 "node1" digest:hzk:123456:crdwa
            zooKeeper.addAuthInfo("digest","hzk:123456".getBytes());
            byte[] bytes = zooKeeper.getData("/node1",false,null);
            System.out.println(new String(bytes));
            Thread.sleep(1000 * 50);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(zooKeeper!=null) {
                try {
                    zooKeeper.close();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
