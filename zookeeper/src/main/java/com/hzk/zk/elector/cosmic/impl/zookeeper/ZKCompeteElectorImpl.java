package com.hzk.zk.elector.cosmic.impl.zookeeper;

import com.hzk.zk.curator.ZKFactory;
import com.hzk.zk.elector.cosmic.Node;
import com.hzk.zk.elector.cosmic.impl.CompeteElector;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

import java.util.UUID;

public class ZKCompeteElectorImpl extends CompeteElector {

    private final CuratorFramework zkClient;
    private final String electPath;
    private final String masterPath;
    private volatile boolean masterFlag = false;
    private final PathChildrenCache childrenCache;
    private final static String MASTER_ID = UUID.randomUUID().toString().substring(0,8);

    public ZKCompeteElectorImpl(String systemName) {
        this(systemName, 3);
    }

    public ZKCompeteElectorImpl(String systemName, int heartbeatIntervalSeconds) {
        super(systemName, heartbeatIntervalSeconds);
        String url = "172.20.70.40:2181,172.20.70.40:2182,172.20.70.40:2183";
        zkClient = ZKFactory.getZKClient(url);
        StringBuilder sb = new StringBuilder().append("/hzk/dts/").append("defaultCluster");
        electPath = sb.toString();
        masterPath = sb.append("/master").toString();
        childrenCache = new PathChildrenCache(zkClient, electPath, true);
    }

    @Override
    protected void doStart() {
        preElect();
        doElect();
    }


    private void preElect() {
        try {
            Stat stat = zkClient.checkExists().forPath(electPath);
            if (stat == null) {
                zkClient.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(electPath);
            }
            childrenCache.start();
            childrenCache.getListenable().addListener((curatorFramework, event) -> {
                switch (event.getType()) {
                    case CHILD_REMOVED:
                        doElect();
                        break;
                    case CONNECTION_RECONNECTED:
                        ensureMaster();
                        break;
                    case CONNECTION_LOST:
                    case CONNECTION_SUSPENDED:
                        masterFlag = false;
                        notifyLostMaster();
                        break;
                    default:
                        break;
                }
            });
        } catch (Exception e) {
            throw new RuntimeException("exception in elect prepare", e);
        }
    }


    private void doElect() {
        try {
            zkClient.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).inBackground((curatorFramework, curatorEvent) -> {
                int resultCode = curatorEvent.getResultCode();
                switch (resultCode) {
                    case 0:
                        masterFlag = true;
                        notifyMaster();
                        break;
                    case -110:
                        masterFlag = false;
                        notifyLostMaster();
                        break;
                    default:
                        break;
                }
            }).forPath(masterPath, MASTER_ID.getBytes());
        } catch (Exception e) {
            throw new RuntimeException("error in electing master node", e);
        }
    }

    private String ensureMaster() {
        try {
            byte[] bytes = zkClient.getData().forPath(masterPath);
            String nodeId = new String(bytes);
            if (MASTER_ID.equals(nodeId)) {
                //masterNode not change
                masterFlag = true;
            } else {
                masterFlag = false;
                notifyLostMaster();
            }
            return nodeId;
        } catch (Exception e) {
            masterFlag = false;
            notifyLostMaster();
            throw new RuntimeException("check masterNode data exception after zk reconnect ", e);
        }
    }


    @Override
    public boolean isMaster() {
        ensureMaster();
        return masterFlag;
    }

    @Override
    public Node getMaster() {
        String nodeId = ensureMaster();
        Node node = new Node();
        node.setInstanceId(nodeId);
        return node;
    }

}
