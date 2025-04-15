package com.hzk.zk.elector.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.apache.curator.framework.state.ConnectionState;

import java.io.Closeable;
import java.io.IOException;

/**
 * 客户端调用选举API
 * https://blog.csdn.net/qq_35958391/article/details/124421865
 */
public class CuratorElectorListenerAdapter extends LeaderSelectorListenerAdapter implements Closeable {

    private final String name;
    private final String path;
    private final LeaderSelector leaderSelector;

    public CuratorElectorListenerAdapter(CuratorFramework client, String path, String name) {
        this.name = name;
        this.path = path;
        leaderSelector = new LeaderSelector(client, path, this);
        leaderSelector.autoRequeue();
    }

    public void start(){
        leaderSelector.start();
    }

    @Override
    public void stateChanged(CuratorFramework client, ConnectionState newState) {
        super.stateChanged(client, newState);

    }

    @Override
    public void takeLeadership(CuratorFramework client) throws Exception {
        System.err.println(name + "现在是leader");
        client.setData()
                .forPath(this.path,("node:" + this.name).getBytes());
//        while (true) {
//            byte[] bytes = client.getData().forPath(this.path);
//            String masterId = new String(bytes);
//
//            Thread.currentThread().sleep(1000 * 5);
//        }
    }


    @Override
    public void close() throws IOException {
        leaderSelector.close();
    }
}
