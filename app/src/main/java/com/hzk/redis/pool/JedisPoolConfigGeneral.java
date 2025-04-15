package com.hzk.redis.pool;

import com.hzk.util.ConfigurationUtil;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisPoolConfig;

public class JedisPoolConfigGeneral {
    private static final Logger logger = LoggerFactory.getLogger(JedisPoolConfigGeneral.class);
    private static final int default_maxTotal = 50;
    private static final boolean default_testWhileIdle = true;
    private static final long default_minEvictableIdleTimeMillis = 30000l;
    private static final int default_numTestsPerEvictionRun = -1;
    private static final int default_maxIdle = 10;
    private static final int default_minIdle = 5;
    private static final int default_maxWaitMillis = 5000;
    private static final boolean default_testOnBorrow = false;
    private static final boolean default_testOnReturn = false;
    private static final long default_timeBetweenEvictionRunsMillis = 30000L;
    private static final boolean default_fairness = false;
    private static final boolean default_lifo = true;
    private static final boolean default_testOnCreate = false;
    private static final long default_softMinEvictableIdleTimeMillis = -1;
    private static final boolean default_blockWhenExhausted = true;
    private static final String default_evictionPolicyClassName = "org.apache.commons.pool2.impl.DefaultEvictionPolicy";
    private static final String POOL_MAX_TOTAL = "redis.pool.maxTotal";

    /**
     * 获取jedis连接池配置
     *
     * @return
     */
    public JedisPoolConfig getPoolConfig() {
        int maxTotal = ConfigurationUtil.getInteger(POOL_MAX_TOTAL, default_maxTotal);
        boolean testWhileIdle = ConfigurationUtil.getBoolean("redis.pool.testWhileIdle", default_testWhileIdle);
        long minEvictableIdleTimeMillis = ConfigurationUtil.getLong("redis.pool.minEvictableIdleTimeMillis", default_minEvictableIdleTimeMillis);
        int numTestsPerEvictionRun = ConfigurationUtil.getInteger("redis.pool.numTestsPerEvictionRun", default_numTestsPerEvictionRun);
        int maxIdle = ConfigurationUtil.getInteger("redis.pool.maxIdle", default_maxIdle);
        int minIdle = ConfigurationUtil.getInteger("redis.pool.minIdle", default_minIdle);
        int maxWaitMillis = ConfigurationUtil.getInteger("redis.pool.maxWaitMillis", default_maxWaitMillis);
        boolean testOnBorrow = ConfigurationUtil.getBoolean("redis.pool.testOnBorrow", default_testOnBorrow);
        //boolean testOnReturn = ConfigurationUtil.getBoolean("redis.pool.default_testOnReturn", default_testOnReturn);
        boolean testOnReturn = ConfigurationUtil.getBoolean("redis.pool.testOnReturn", default_testOnReturn);
        long timeBetweenEvictionRunsMillis = ConfigurationUtil.getLong("redis.pool.timeBetweenEvictionRunsMillis", default_timeBetweenEvictionRunsMillis);
        boolean fairness = ConfigurationUtil.getBoolean("redis.pool.fairness", default_fairness);
        boolean lifo = ConfigurationUtil.getBoolean("redis.pool.lifo", default_lifo);
        boolean testOnCreate = ConfigurationUtil.getBoolean("redis.pool.testOnCreate", default_testOnCreate);
        long softMinEvictableIdleTimeMillis = ConfigurationUtil.getLong("redis.pool.softMinEvictableIdleTimeMillis", default_softMinEvictableIdleTimeMillis);
        boolean blockWhenExhausted = ConfigurationUtil.getBoolean("redis.pool.blockWhenExhausted", default_blockWhenExhausted);
        String evictionPolicyClassName = ConfigurationUtil.getString("redis.pool.evictionPolicyClassName", default_evictionPolicyClassName);

        /**
         * jedisPool 默认为TestWhileIdle=true;minEvictableIdleTimeMillis=60000L;numTestsPerEvictionRun=-1
         */
        JedisPoolConfig config = new JedisPoolConfig();

        //在连接池空闲时是否测试连接对象的有效性,默认false
        config.setTestWhileIdle(testWhileIdle);
        //设置连接最小的逐出间隔时间，默认1800000毫秒
        config.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        //设置连接对象有效性扫描间隔,设置为-1,则不运行逐出线程
        config.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        //每次逐出检查时,逐出连接的个数, 默认为3
        config.setNumTestsPerEvictionRun(numTestsPerEvictionRun);
        config.setMaxTotal(maxTotal);
        config.setMaxIdle(maxIdle);
        //设置无连接时池中最小的连接个数,默认连接0
        config.setMinIdle(minIdle);
        config.setMaxWaitMillis(maxWaitMillis); // connect timeout
        //从池中获取连接时是否测试连接的有效性,默认false
        config.setTestOnBorrow(testOnBorrow);
        //在连接对象返回时，是否测试对象的有效性,默认false
        config.setTestOnReturn(testOnReturn);
        //在连接对象创建时测试连接对象的有效性,默认false
        config.setTestOnCreate(testOnCreate);
        //当池中的资源耗尽时是否进行阻塞,设置false直接报错,true表示会一直等待，直到有可用资源
        config.setBlockWhenExhausted(blockWhenExhausted);
        //设置逐出策略,默认策略为 "org.apache.commons.pool2.impl.DefaultEvictionPolicy"
        config.setEvictionPolicyClassName(evictionPolicyClassName);
        //当从池中获取资源或者将资源还回池中时 是否使用java.util.concurrent.locks.ReentrantLock.ReentrantLock 的公平锁机制,默认为false
        config.setFairness(fairness);
        //对象空闲多久后逐出, 当空闲时间>该值 且 空闲连接>最大空闲数 时直接逐出,不再根据MinEvictableIdleTimeMillis判断
        config.setSoftMinEvictableIdleTimeMillis(softMinEvictableIdleTimeMillis);
        //设置连接对象是否后进先出,默认true
        config.setLifo(lifo);

        return config;
    }

    public GenericObjectPoolConfig getGenericObjectPoolConfig() {
        int maxTotal = ConfigurationUtil.getInteger(POOL_MAX_TOTAL, default_maxTotal);
        boolean testWhileIdle = ConfigurationUtil.getBoolean("redis.pool.testWhileIdle", default_testWhileIdle);
        long minEvictableIdleTimeMillis = ConfigurationUtil.getLong("redis.pool.minEvictableIdleTimeMillis", default_minEvictableIdleTimeMillis);
        int numTestsPerEvictionRun = ConfigurationUtil.getInteger("redis.pool.numTestsPerEvictionRun", default_numTestsPerEvictionRun);
        int maxIdle = ConfigurationUtil.getInteger("redis.pool.maxIdle", default_maxIdle);
        int minIdle = ConfigurationUtil.getInteger("redis.pool.minIdle", default_minIdle);
        int maxWaitMillis = ConfigurationUtil.getInteger("redis.pool.maxWaitMillis", default_maxWaitMillis);
        boolean testOnBorrow = ConfigurationUtil.getBoolean("redis.pool.testOnBorrow", default_testOnBorrow);
        boolean testOnReturn = ConfigurationUtil.getBoolean("redis.pool.testOnReturn", default_testOnReturn);
        long timeBetweenEvictionRunsMillis = ConfigurationUtil.getLong("redis.pool.timeBetweenEvictionRunsMillis", default_timeBetweenEvictionRunsMillis);
        boolean fairness = ConfigurationUtil.getBoolean("redis.pool.fairness", default_fairness);
        boolean lifo = ConfigurationUtil.getBoolean("redis.pool.lifo", default_lifo);
        boolean testOnCreate = ConfigurationUtil.getBoolean("redis.pool.testOnCreate", default_testOnCreate);
        long softMinEvictableIdleTimeMillis = ConfigurationUtil.getLong("redis.pool.softMinEvictableIdleTimeMillis", default_softMinEvictableIdleTimeMillis);
        boolean blockWhenExhausted = ConfigurationUtil.getBoolean("redis.pool.blockWhenExhausted", default_blockWhenExhausted);
        String evictionPolicyClassName = ConfigurationUtil.getString("redis.pool.evictionPolicyClassName", default_evictionPolicyClassName);
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        config.setTestWhileIdle(testWhileIdle);
        config.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        config.setNumTestsPerEvictionRun(numTestsPerEvictionRun);
        config.setMaxTotal(maxTotal);
        config.setMaxIdle(maxIdle);
        config.setMinIdle(minIdle);
        config.setMaxWaitMillis(maxWaitMillis); // connect timeout
        config.setTestOnBorrow(testOnBorrow);
        config.setTestOnReturn(testOnReturn);
        config.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        //在连接对象创建时测试连接对象的有效性,默认false
        config.setTestOnCreate(testOnCreate);
        //当池中的资源耗尽时是否进行阻塞,设置false直接报错,true表示会一直等待，直到有可用资源
        config.setBlockWhenExhausted(blockWhenExhausted);
        //设置逐出策略,默认策略为 "org.apache.commons.pool2.impl.DefaultEvictionPolicy"
        config.setEvictionPolicyClassName(evictionPolicyClassName);
        //当从池中获取资源或者将资源还回池中时 是否使用java.util.concurrent.locks.ReentrantLock.ReentrantLock 的公平锁机制,默认为false
        config.setFairness(fairness);
        //对象空闲多久后逐出, 当空闲时间>该值 且 空闲连接>最大空闲数 时直接逐出,不再根据MinEvictableIdleTimeMillis判断
        config.setSoftMinEvictableIdleTimeMillis(softMinEvictableIdleTimeMillis);
        //设置连接对象是否后进先出,默认true
        config.setLifo(lifo);

        return config;
    }
}
