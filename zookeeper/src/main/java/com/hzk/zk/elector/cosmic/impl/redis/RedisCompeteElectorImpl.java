//package com.hzk.zk.elector.cosmic.impl.redis;
//
//import com.hzk.zk.elector.cosmic.impl.CompeteElector;
//import kd.bos.cache.CacheConfigKeys;
//import kd.bos.elect.Node;
//import kd.bos.elect.impl.CompeteElector;
//import kd.bos.instance.Instance;
//import kd.bos.logging.Log;
//import kd.bos.logging.LogFactory;
//import kd.bos.redis.JedisClient;
//import kd.bos.redis.RedisFactory;
//import redis.clients.jedis.exceptions.JedisConnectionException;
//
//import java.util.Timer;
//import java.util.TimerTask;
//import java.util.concurrent.atomic.AtomicLong;
//
//public class RedisCompeteElectorImpl extends CompeteElector {
//    private final static String NX = "NX";
//    private final static String EX = "EX";
//
//    private volatile boolean masterFlag = false;
//    private AtomicLong lease = new AtomicLong();
//    private static final Log log = LogFactory.getLog(RedisCompeteElectorImpl.class);
//
//    public RedisCompeteElectorImpl(String systemName) {
//        super(systemName);
//    }
//
//    public RedisCompeteElectorImpl(String systemName, int heartbeatIntervalSeconds) {
//        super(systemName, heartbeatIntervalSeconds);
//    }
//
//    protected JedisClient createJedisClient() {
//        return RedisFactory.getJedisClient(getRedisUrl());
//    }
//
//    private String getRedisUrl() {
//        String url = null;
//
//        String useRegion = "elect";
//
//        String configKey = CacheConfigKeys.getSessionlessConfigKey(useRegion);
//        url = System.getProperty(configKey);
//
//        if (url == null) {
//            configKey = CacheConfigKeys.getSessionableConfigKey();
//            url = System.getProperty(configKey);
//        }
//        return url;
//    }
//
//    @Override
//    protected void doStart() {
//
//        Timer timer = new Timer("RedisCompeteElector-" + getElectClusterName());
//        timer.scheduleAtFixedRate(new TimerTask() {
//            @Override
//            public void run() {
//                try {
//                    doElect();
//                } catch (Exception | Error e) {
//                    log.warn("doElect error", e);
//                }
//            }
//
//        }, 1, getHeartbeatIntervalSeconds() * 1000);
//    }
//
//    private void doElect() {
//        if (Instance.isPausedServiceByMonitor()) {
//            if (masterFlag) {
//                lostMaster();
//            }
//            return;
//        }
//
//        try (JedisClient jedis = createJedisClient();) {
//            if (masterFlag) {
//                if (lease.get() < System.currentTimeMillis()) {
//                    lostMaster();
//                } else {
//                    if (1 == jedis.expire(masterKey, getHeartbeatTimeOutSeconds())) {
//                        if (lease.get() < System.currentTimeMillis()
//                                || !Instance.getInstanceId().equals(jedis.get(masterKey))) {
//                            lostMaster();
//                        } else {
//                            setLease();
//                            log.info(masterKey + ", masterNodeIs :" + Instance.getInstanceId());
//                        }
//                    }
//                }
//            } else {
//                String status = jedis.set(masterKey, Instance.getInstanceId(), NX, EX, getHeartbeatTimeOutSeconds());
//                if ("OK".equals(status)) {
//                    becomeMaster();
//                }
//            }
//        } catch (Exception e) {
//            Throwable cause = e.getCause();
//            if (cause != null) {
//                if (masterFlag && cause instanceof JedisConnectionException) {
//                    setLease();
//                }
//            }
//        }
//
//    }
//
//    private void setLease() {
//        lease.set(System.currentTimeMillis() + getHeartbeatTimeOutSeconds() * 1000);
//    }
//
//    private void lostMaster() {
//        masterFlag = false;
//        // notify events
//        notifyLostMaster();
//    }
//
//    private void becomeMaster() {
//        masterFlag = true;
//        setLease();// set lease time
//        notifyMaster();
//    }
//
//    @Override
//    public boolean isMaster() {
//        checkStarted();
//        if (lease.get() < System.currentTimeMillis()) {
//            doElect();
//        }
//
//        return masterFlag;
//    }
//
//    @Override
//    public Node getMaster() {
////        checkStarted();
//        try (JedisClient jedis = createJedisClient();) {
//            String master = jedis.get(masterKey);
//            Node node = new Node();
//            if (master != null) {
//                node.setInstanceId(master);//TODO get more info
//            }
//            return node;
//        }
//    }
//}
