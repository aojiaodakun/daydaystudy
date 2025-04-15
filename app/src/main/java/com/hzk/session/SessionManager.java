package com.hzk.session;

import com.alibaba.fastjson.JSON;
import com.hzk.redis.RedisFactory;
import com.hzk.util.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import redis.clients.jedis.Jedis;

import java.util.Date;

/**
 * 用户会话管理器
 * TODO
 * 后台线程定时清除过期session
 */
public class SessionManager {

    private static final String redisUrl = System.getProperty("redis.serversForCache", "127.0.0.1:6379");
    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 校验会话是否存在
     *
     * @param globalSessionId globalSessionId
     * @return 布尔值，会话是否有效
     */
    public static boolean checkSession(String globalSessionId) {
        try (Jedis jedisClient = RedisFactory.getJedisClient(redisUrl)) {
            String sessionValue = jedisClient.get(globalSessionId);
            if (StringUtils.isEmpty(sessionValue)) {
                return false;
            }
            SessionInfo sessionInfo = JSON.parseObject(sessionValue, SessionInfo.class);
            if (sessionInfo != null && StringUtils.isNotEmpty(sessionInfo.getUserName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 生成新的会话
     *
     * @param username username
     * @return 全局会话globalSessionId
     */
    public static String newSession(String username) {
        String globalSessionId = "";
        try (Jedis jedisClient = RedisFactory.getJedisClient(redisUrl)) {
            SessionInfo sessionInfo = new SessionInfo();
            sessionInfo.setUserName(username);
            sessionInfo.setLoginTime(DateFormatUtils.format(new Date(), DATE_TIME_FORMAT));
            String sessionInfoJsonString = JSON.toJSONString(sessionInfo);
            // 生成globalSessionId
            globalSessionId = getSessionIDString();
            jedisClient.set(globalSessionId, sessionInfoJsonString);
        }
        return globalSessionId;
    }


    private static String getSessionIDString() {
        return new StringBuilder().append("_").append(StringUtils.randomWord(100)).append(getHourSubOfDay()).toString();
    }

    private static String getHourSubOfDay() {
        long curTime = System.currentTimeMillis();
        long timeOfDay = curTime % (1000 * 3600 * 24);
        int hour = (int) timeOfDay / (1000 * 3600);
        return hour <= 9 ? "0" + hour : "" + hour;
    }

}
