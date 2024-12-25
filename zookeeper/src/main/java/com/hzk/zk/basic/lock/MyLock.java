package com.hzk.zk.basic.lock;

import com.hzk.zk.constants.BasicConstants;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class MyLock {


    static ZooKeeper zooKeeper;

    static String LOCK_ROOT_PATH = "/locks";

    String LOCK_NODE_NAME = "lock_";
    // 锁节点路径
    String lockPath;

    static {
        try{
            //创建一个计数器对象
            CountDownLatch countDownLatch = new CountDownLatch(1);
            //第一个参数是服务器ip和端口号，第二个参数是客户端与服务器的会话超时时间单位ms，第三个参数是监视器对象
            zooKeeper = new ZooKeeper(BasicConstants.IP, 1000 * 20, new Watcher() {
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
            Stat stat = zooKeeper.exists(LOCK_ROOT_PATH,false);
            if (stat == null) {
                zooKeeper.create(LOCK_ROOT_PATH,new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    Watcher watcher = new Watcher() {
        @Override
        public void process(WatchedEvent watchedEvent) {
            if(watchedEvent.getType() == Event.EventType.NodeDeleted){
                synchronized (this){
                    notifyAll();
                }
            }
        }
    };

    // 获取锁
    public void acquireLock() throws Exception{
        createLock();
        attemptLock();
    }

    // 创建锁节点
    public void createLock() throws Exception{
        // 临时有序节点
        lockPath = zooKeeper.create(LOCK_ROOT_PATH + "/" + LOCK_NODE_NAME,new byte[0],ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.EPHEMERAL_SEQUENTIAL);
        System.out.println(Thread.currentThread().getName() + ",节点创建成功:" + lockPath);
    }


    // 尝试获取锁
    public void attemptLock() throws Exception{
        List<String> childrenList = zooKeeper.getChildren(LOCK_ROOT_PATH, false);
        Collections.sort(childrenList);
        // /locks/lock_0000000000
        int index = childrenList.indexOf(lockPath.substring(LOCK_ROOT_PATH.length()+1));
        if(index == 0){
            System.out.println(Thread.currentThread().getName() + "获取锁成功");
            return;
        }else{
            String path = childrenList.get(index -1);
            Stat stat = zooKeeper.exists(LOCK_ROOT_PATH + "/" + path,watcher);
            if(stat == null){
                attemptLock();
            }else{
                synchronized (watcher){
                    watcher.wait();
                }
                attemptLock();
            }
        }
    }

    // 释放锁
    public void releaseLock() throws Exception{
        zooKeeper.delete(this.lockPath,-1);
        System.out.println(Thread.currentThread().getName() + "锁已经释放:" + this.lockPath);
    }

}
