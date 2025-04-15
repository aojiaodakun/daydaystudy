package com.hzk.zk.archaius;

import java.util.Iterator;

import java.io.Closeable;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import com.google.common.io.Closeables;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent.Type;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.zookeeper.KeeperException.NoNodeException;
import org.apache.zookeeper.KeeperException.NodeExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netflix.config.WatchedUpdateListener;
import com.netflix.config.WatchedUpdateResult;
import com.netflix.config.WatchedConfigurationSource;


public class ZooKeeperConfigurationSource implements WatchedConfigurationSource, Closeable {
    private static final Logger logger = LoggerFactory.getLogger(ZooKeeperConfigurationSource.class);

    private final CuratorFramework client;
    private final String configRootPath;
    private final PathChildrenCache pathChildrenCache;

    private final Charset charset = Charset.forName("UTF-8");

    private List<WatchedUpdateListener> listeners = new CopyOnWriteArrayList<WatchedUpdateListener>();

    /**
     * Creates the pathChildrenCache using the CuratorFramework client and ZK root path node for the config
     *
     */
    public ZooKeeperConfigurationSource(CuratorFramework client, String configRootPath) {
        this.client = client;
        this.configRootPath = configRootPath;
        this.pathChildrenCache = new PathChildrenCache(client, configRootPath, true);
    }

    /**
     * Adds a listener to the pathChildrenCache, initializes the cache, then starts the cache-management background thread
     *
     */
    public void start() {
        // create the watcher for future configuration updatess
        pathChildrenCache.getListenable().addListener(new PathChildrenCacheListener() {
            public void childEvent(CuratorFramework aClient, PathChildrenCacheEvent event) {
                Type eventType = event.getType();
                ChildData data = event.getData();

                String path = null;
                if (data != null) {
                    path = data.getPath();

                    // scrub configRootPath out of the key name
                    String key = removeRootPath(path);

                    byte[] value = data.getData();
                    String stringValue = new String(value, charset);

                    logger.debug("received update to pathName [{}], eventType [{}]", path, eventType);
                    logger.debug("key [{}], and value [{}]", key, stringValue);

                    // fire event to all listeners
                    Map<String, Object> added = null;
                    Map<String, Object> changed = null;
                    Map<String, Object> deleted = null;
                    if (eventType == Type.CHILD_ADDED) {
                        added = new HashMap<String, Object>(1);
                        added.put(key, stringValue);
                    } else if (eventType == Type.CHILD_UPDATED) {
                        changed = new HashMap<String, Object>(1);
                        changed.put(key, stringValue);
                    } else if (eventType == Type.CHILD_REMOVED) {
                        deleted = new HashMap<String, Object>(1);
                        deleted.put(key, stringValue);
                    }

                    WatchedUpdateResult result = WatchedUpdateResult.createIncremental(added,
                            changed, deleted);

                    fireEvent(result);
                }
            }
        });

        // passing true to trigger an initial rebuild upon starting.  (blocking call)
        try {
            pathChildrenCache.start(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Map<String, Object> getCurrentData() {
        logger.debug("getCurrentData() retrieving current data.");
        try {
            List<ChildData> children = pathChildrenCache.getCurrentData();
            Map<String, Object> all = new HashMap<String, Object>(children.size());
            for (ChildData child : children) {
                String path = child.getPath();
                String key = removeRootPath(path);
                byte[] value = child.getData();

                all.put(key, new String(value, charset));
            }

            logger.debug("getCurrentData() retrieved [{}] config elements.", children.size());
            return all;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void addUpdateListener(WatchedUpdateListener l) {
        if (l != null) {
            listeners.add(l);
        }
    }

    @Override
    public void removeUpdateListener(WatchedUpdateListener l) {
        if (l != null) {
            listeners.remove(l);
        }
    }

    protected void fireEvent(WatchedUpdateResult result) {
        for (WatchedUpdateListener l : listeners) {
            try {
                l.updateConfiguration(result);
            } catch (Exception ex) {
                logger.error("Error in invoking WatchedUpdateListener", ex);
            }
        }
    }

    /**
     * This is used to convert a configuration nodePath into a key
     *
     * @param nodePath
     *
     * @return key (nodePath less the config root path)
     */
    private String removeRootPath(String nodePath) {
        return nodePath.replace(configRootPath + "/", "");
    }


    //@VisibleForTesting
    synchronized void setZkProperty(String key, String value) {
        final String path = configRootPath + "/" + key;

        byte[] data = value.getBytes(charset);

        try {
            // attempt to create (intentionally doing this instead of checkExists())
            client.create().creatingParentsIfNeeded().forPath(path, data);
        } catch (NodeExistsException exc) {
            // key already exists - update the data instead
            try {
                client.setData().forPath(path, data);
            } catch (Exception e) {
                e.printStackTrace();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //@VisibleForTesting
    synchronized String getZkProperty(String key) {
        final String path = configRootPath + "/" + key;

        byte[] bytes = new byte[0];
        try {
            bytes = client.getData().forPath(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new String(bytes, charset);
    }

    //@VisibleForTesting
    synchronized void deleteZkProperty(String key) {
        final String path = configRootPath + "/" + key;

        try {
            client.delete().forPath(path);
        } catch (NoNodeException exc) {
            // Node doesn't exist - NoOp
            logger.warn("Node doesn't exist", exc);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            Closeables.close(pathChildrenCache, true);
        } catch (IOException exc) {
            logger.error("IOException should not have been thrown.", exc);
        }
    }
}