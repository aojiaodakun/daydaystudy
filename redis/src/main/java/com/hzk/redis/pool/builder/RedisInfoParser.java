package com.hzk.redis.pool.builder;

import com.hzk.redis.pool.bean.HostAndPort;
import com.hzk.redis.pool.bean.RedisInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Locale;
import java.util.Properties;
import java.util.Set;

/**
 * @author rd_lixun_tang
 * redis:172.17.7.78:6379
 * redis:172.17.7.78:6379/?$password
 * redis:172.17.7.78:6379/passwordxxx
 * redis:172.17.7.78:6379/?vendor=tong&password=xx
 * sentinel:172.17.7.78:6379;172.17.7.78:6379;172.17.7.78:6379/?mymaster=mymaster&password=123
 * cluster:172.20.187.93:7379;172.20.187.93:7378;172.20.187.94:7379;172.20.187.94:7378;172.20.187.95:7379;172.20.187.95:7378/KDadm_2023
 */
public class RedisInfoParser {
    private static final Logger logger = LoggerFactory.getLogger(RedisInfoParser.class);
    private static final String PASS = "password";

    public static RedisInfo parseUrl(String url) {
        if (StringUtils.isBlank(url)) {
            throw new NullPointerException("redis url can not be null!");
        }

        url = dropTypePrefix(url);

        url = dropSSLPrefix(url);

        RedisInfo redisInfo = new RedisInfo();

        //String[] parts = url.split('/');(password可能是加密过的会出现多个斜杠)
        int index = url.indexOf('/');
        String[] parts = null;
        if (index != -1) {
            parts = new String[]{url.substring(0, index), url.substring(index + 1)};
        } else {
            parts = new String[]{url};
        }

        if (parts.length == 2) {
            String part1 = parts[1];
            if (part1.startsWith("?") && part1.contains("=")) {
                part1 = part1.substring(1);
                Properties e = parseParm(part1);
                redisInfo.setProperties(e);
                if(e!=null&&StringUtils.isNotEmpty(e.getProperty(PASS))){
                    redisInfo.setPassword(e.getProperty(PASS));
                }
            } else {
                String password = parts[1];
                redisInfo.setPassword(password);
            }
        }

        String server = parts[0];
        redisInfo.setHostAndPorts(parseServer(server));

        return redisInfo;
    }

    /**
     * Trim redis url, remove password
     * @param url redis/xcache url
     * @return url without password
     */
    public static String trimUrl(String url) {
        if (url == null) {
            return null;
        }
        int spiltIndex = url.lastIndexOf("/");
        if (spiltIndex < 0) {
            return url;
        }
        return url.substring(0, spiltIndex);
    }


    private static String dropTypePrefix(String url)  {
        String poolUrl ;

        if (url.toLowerCase(Locale.ENGLISH).startsWith("redis:")) {
            poolUrl = url.substring("redis:".length());
        } else if (url.toLowerCase(Locale.ENGLISH).startsWith("shard:")) {
            poolUrl = url.substring("shard:".length());
        } else if (url.toLowerCase(Locale.ENGLISH).startsWith("sentinel:")) {
            poolUrl = url.substring("sentinel:".length());
        } else if (url.toLowerCase(Locale.ENGLISH).startsWith("cluster:")) {
            poolUrl = url.substring("cluster:".length());
        } else {
            poolUrl = url;
        }

        return poolUrl;

    }

    private static String dropSSLPrefix(String poolUrl)  {
        //使用格式为： System.setProperty("redis.serversForCache", "redis:ssl:ip:port/password");
        boolean ssl = poolUrl.toLowerCase(Locale.ENGLISH).startsWith("ssl:");
        poolUrl = ssl ? poolUrl.substring(4) : poolUrl;

        return poolUrl;

    }


    private static Properties parseParm(String part1) {
        String[] params = null;

        if (part1.contains("&")) {
            params = part1.split("&");
        } else {
            params = new String[]{part1};
        }
        Properties prop = new Properties();
        for (String p : params) {
            if (p.contains("=")) {
                int index = p.indexOf("=");
                String key = p.substring(0, index);
                String value = p.substring(index + 1);
                prop.put(key, value);
            }
        }
        return prop;
    }

    private static Set<HostAndPort> parseServer(String server) {
        Set<HostAndPort> hostAndPorts = new HashSet<>();
        String parts[] = server.split(";|,");
        try {
            for (int i = 0; i < parts.length; i++) {
                String host = parts[i].trim();
                String[] hap =parseIpPort(host);
                if (hap.length != 2) {
                    logger.error("wrong redis config:" + host);
                    continue;
                }

                hostAndPorts.add(new HostAndPort(hap[0], Integer.parseInt(hap[1].trim())));
            }

            return hostAndPorts;
        } catch (Exception t) {
            logger.error("error parse redis server:" + server, t);
            throw new IllegalArgumentException("error parse redis server:" + server);
        }
    }

    private static String[] parseIpPort(String address){
        String[] arg = new String[2];
        int v6i = address.lastIndexOf("]");
        if (address.startsWith("[") && v6i > 0) {
            // 解析ipV6地址
            arg[0] = address.substring(1,address.lastIndexOf("]"));
            arg[1] = address.substring(v6i+2);
        }else {
            arg = address.split(":");
        }
        return arg;
    }


}

