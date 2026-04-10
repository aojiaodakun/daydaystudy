package com.hzk.zookeeper.factory;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.ACLProvider;
import org.apache.curator.retry.RetryForever;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooDefs.Perms;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public class ZKFactory {
    private static final String ZKADDR_STR = "zkaddreass";
    private static final String ZKROOTPATH_STR = "zkrootpath";
    private static final String ZKSCHEME_STR = "scheme";
    private static final String ZKUSER_STR = "user";
    private static final String ZKPASS_STR = "password";
    public static final ConcurrentHashMap<String, CuratorFramework> poolMap = new ConcurrentHashMap<>();//NOSONAR
    private static final Logger logger = LoggerFactory.getLogger(ZKFactory.class);
    public static Map<String, Map<String, String>> parsedUrlMap = new ConcurrentHashMap<>();
    private final static String ZK_TRACE_NAME = "ZKFactory";
    static{
        //解决zk升级后，zk集群出现连接中断或超时异常
        System.setProperty("zookeeper.sasl.client",System.getProperty("zookeeper.sasl.client","false"));//NOSONAR
        System.setProperty("jdk.tls.rejectClientInitiatedRenegotiation",System.getProperty("jdk.tls.rejectClientInitiatedRenegotiation","true"));//NOSONAR
        Runtime.getRuntime().addShutdownHook(new Thread(()->{
            poolMap.forEach((k,v)->{
                try {
                    v.getZookeeperClient().getZooKeeper().close();
                    logger.info("close zookeeperConnection on shutdown");
                } catch (Exception e) {
                    logger.error("error on ShutdownHook of "+ k,e);
                }
            });
        }));
    }

    static Map<String, CuratorFramework> getPool() {
        return poolMap;
    }

    static void put(String url,CuratorFramework curatorFramework) {
        poolMap.put(url,curatorFramework);
    }

    private static Map<String, String> parseUrl(String url) {
        Map<String, String> m = parsedUrlMap.get(url);
        if (m != null) {
            return m;
        }
        m = new ConcurrentHashMap<>();

        int propIndex = url.indexOf('?');
        String _url = url;
        if (propIndex > 0) {
            String[] sl = url.split("\\?");
            _url = sl[0];
            String strUrlParams = sl[1];
            Properties prop = new Properties();
            try {
                String[] params = null;

                if (strUrlParams.contains("&")) {
                    params = strUrlParams.split("&");
                } else {
                    params = new String[]{strUrlParams};
                }

                for (String p : params) {
                    if(p.startsWith(ZKPASS_STR)){
                        String password  =  p.substring(ZKPASS_STR.length()+1);
                        prop.put(ZKPASS_STR, password);
                    } else if (p.contains("=")) {
                        String[] param = p.split("=");
                        if (param.length == 1) {
                            prop.put(param[0], "");
                        } else {

                            String key = param[0];
                            String value = param[1];

                            prop.put(key, value);
                        }
                    }
//                    else {
//
//                    }
                }
                m.put(ZKSCHEME_STR, prop.getProperty(ZKSCHEME_STR, "digest"));
                m.put(ZKUSER_STR, prop.getProperty(ZKUSER_STR));
                String pass = prop.getProperty(ZKPASS_STR);
                if (pass != null) {
                    m.put(ZKPASS_STR, pass);
                }
            } catch (Exception e) {
                logger.error("parse zookeeper url exception", e);
            }
        }
        m.put(ZKADDR_STR, _getZkAddress(_url));
        m.put(ZKROOTPATH_STR, _getZkRootPath(_url));
        parsedUrlMap.put(url, m);

        return m;
    }

    public static CuratorFramework getZKClient(String url) {
        try {
            Map<String, String> urlInfo = parseUrl(url);
            url = urlInfo.get(ZKADDR_STR);
            if (poolMap.containsKey(url)) {
                return poolMap.get(url);
            } else {
                synchronized (ZKFactory.class) {
                    if (poolMap.containsKey(url)) {
                        return poolMap.get(url);
                    }
                    RetryPolicy retryPolicy = new RetryForever(Integer.getInteger("zookeeper.client.retry.intervalMs", 10000));
                    CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory
                            .builder().connectString(url)
                            .sessionTimeoutMs(Integer.getInteger("curator-default-session-timeout", ''))
                            .connectionTimeoutMs(Integer.getInteger("curator-default-connection-timeout", 15000))
                            .retryPolicy(retryPolicy);
                    String user = urlInfo.get(ZKUSER_STR);
                    String pass = urlInfo.get(ZKPASS_STR);
                    if (user != null && pass != null) {
                        String auth = user + ":" + pass;

                        ACLProvider aclProvider = new ACLProvider() {
                            private List<ACL> acl;

                            @Override
                            public List<ACL> getDefaultAcl() {
                                if (acl == null) {
                                    ArrayList<ACL> acl = ZooDefs.Ids.CREATOR_ALL_ACL; // 初始化
                                    acl.clear();
                                    acl.add(new ACL(Perms.ALL, new Id("auth", auth)));// 添加
                                    this.acl = acl;
                                }
                                return acl;
                            }

                            @Override
                            public List<ACL> getAclForPath(String path) {
                                return acl;
                            }
                        };
                        builder.authorization(urlInfo.get(ZKSCHEME_STR), (auth).getBytes());
                        builder.aclProvider(aclProvider);
                    }
                    CuratorFramework client = builder.build();
                    client.start();
                    poolMap.put(url, client);
                    return client;
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        }
    }
    public static String getAuthority(String url) {
        Map<String, String> urlInfo = parseUrl(url);
        String user = urlInfo.get(ZKUSER_STR);
        String pass = urlInfo.get(ZKPASS_STR);
        if (user != null && pass != null) {
            return user + ":" + pass;
        }
        return null;
    }
    public static String getZkAddress(String url) {
        Map<String, String> urlInfo = parseUrl(url);
        return urlInfo.get(ZKADDR_STR);
    }

    private static String _getZkAddress(String url) {
        int index = url.indexOf('/');
        if (index > 0) {
            return url.substring(0, index);
        } else {
            return url;
        }
    }

    /**
     * @param url
     * @return end with "/"
     */
    public static String getZkRootPath(String url) {
        Map<String, String> urlInfo = parseUrl(url);
        return urlInfo.get(ZKROOTPATH_STR);
    }

    private static String _getZkRootPath(String url) {
        int index = url.indexOf('/');
        if (index > 0) {
            String r = url.substring(index);
            if (!r.endsWith("/")) {
                r += "/";
            }
            return r;
        } else {
            return "/";
        }
    }

}
