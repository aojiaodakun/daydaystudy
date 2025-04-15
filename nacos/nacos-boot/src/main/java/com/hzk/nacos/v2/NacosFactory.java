package com.hzk.nacos.v2;

import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingMaintainService;
import com.alibaba.nacos.api.naming.NamingService;
import org.apache.log4j.Logger;

import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 配置中心客户端、注册中心客户端
 */
public class NacosFactory {

    private static final Logger LOGGER = Logger.getLogger(NacosFactory.class);

    private static final String NAMESPACE_STR = "namespace";
    private static final String USER_STR = "user";
    private static final String PASSWORD_STR = "password";

    private static final ConcurrentHashMap<String, NacosConfigService> SERVERADDR_CONFIGSERVICE_MAP = new ConcurrentHashMap<>();//NOSONAR

    private static final ConcurrentHashMap<String, NacosNamingService> SERVERADDR_NAMINGSERVICE_MAP = new ConcurrentHashMap<>();//NOSONAR

    private static final ConcurrentHashMap<String, NacosNamingMaintainClient> SERVERADDR_NAMINGMAINTAINSERVICE_MAP = new ConcurrentHashMap<>();//NOSONAR

    public static ConcurrentHashMap<String, NacosConfigService>  getConfigserviceMap(){
        // 组件测速获取
        return SERVERADDR_CONFIGSERVICE_MAP;
    }

    public static NacosConfigService getNacosConfigClient(String url) {
        Properties properties = getNacosProperties(url);
        String serverAddr = properties.getProperty(PropertyKeyConst.SERVER_ADDR);
        NacosConfigService nacosConfigService = SERVERADDR_CONFIGSERVICE_MAP.get(serverAddr);
        if(nacosConfigService != null) {
            return nacosConfigService;
        }
        synchronized (NacosFactory.class) {
            nacosConfigService = SERVERADDR_CONFIGSERVICE_MAP.get(serverAddr);
            if(nacosConfigService != null) {
                return nacosConfigService;
            }
            try {
                ConfigService configService = com.alibaba.nacos.api.NacosFactory.createConfigService(properties);
                NacosConfigClient nacosConfigClient = new NacosConfigClient(serverAddr, configService);
                SERVERADDR_CONFIGSERVICE_MAP.put(serverAddr, nacosConfigClient);
                return nacosConfigClient;
            } catch (NacosException e) {
                e.printStackTrace();
                LOGGER.error("createConfigService error,url=" + url, e);
                throw new RuntimeException("createConfigService error,url=" + url);
            }
        }
    }

    public static NacosNamingService getNacosNamingService(String url) {
        Properties properties = getNacosProperties(url);
        String serverAddr = properties.getProperty(PropertyKeyConst.SERVER_ADDR);
        NacosNamingService nacosNamingService = SERVERADDR_NAMINGSERVICE_MAP.get(serverAddr);
        if(nacosNamingService != null) {
            return nacosNamingService;
        }
        synchronized (NacosFactory.class) {
            nacosNamingService = SERVERADDR_NAMINGSERVICE_MAP.get(serverAddr);
            if(nacosNamingService != null) {
                return nacosNamingService;
            }
            try {
                NamingService namingService = com.alibaba.nacos.api.NacosFactory.createNamingService(properties);
                NacosNamingClient nacosNamingClient = new NacosNamingClient(serverAddr, namingService);
                SERVERADDR_NAMINGSERVICE_MAP.put(serverAddr, nacosNamingClient);
                return nacosNamingClient;
            } catch (NacosException e) {
                LOGGER.error("createNamingService error,url=" + url, e);
                throw new RuntimeException("createNamingService error,url=" + url);
            }
        }
    }

    public static NacosNamingMaintainClient getNacosNamingMaintainService(String url) {
        Properties properties = getNacosProperties(url);
        String serverAddr = properties.getProperty(PropertyKeyConst.SERVER_ADDR);
        NacosNamingMaintainClient nacosNamingMaintainService = SERVERADDR_NAMINGMAINTAINSERVICE_MAP.get(serverAddr);
        if(nacosNamingMaintainService != null) {
            return nacosNamingMaintainService;
        }
        synchronized (NacosFactory.class) {
            nacosNamingMaintainService = SERVERADDR_NAMINGMAINTAINSERVICE_MAP.get(serverAddr);
            if(nacosNamingMaintainService != null) {
                return nacosNamingMaintainService;
            }
            try {
                NamingMaintainService maintainService = com.alibaba.nacos.api.NacosFactory.createMaintainService(properties);
                NacosNamingMaintainClient nacosNamingMaintainClient = new NacosNamingMaintainClient(serverAddr, maintainService);
                SERVERADDR_NAMINGMAINTAINSERVICE_MAP.put(serverAddr, nacosNamingMaintainClient);
                return nacosNamingMaintainClient;
            } catch (NacosException e) {
                LOGGER.error("createamingMaintainService error,url=" + url, e);
                throw new RuntimeException("createamingMaintainService error,url=" + url);
            }
        }
    }

    static void removeConfigService(String serverAddress) {
        SERVERADDR_CONFIGSERVICE_MAP.remove(serverAddress);
    }

    static void removeNamingService(String serverAddress) {
        SERVERADDR_NAMINGSERVICE_MAP.remove(serverAddress);
    }

    static void removeNamingMaintainService(String serverAddress) {
        SERVERADDR_NAMINGMAINTAINSERVICE_MAP.remove(serverAddress);
    }

    public static Properties getNacosProperties(String url) {
        Properties properties = new Properties();
        String serverAddr = url.split("/")[0];
        if (serverAddr.indexOf("?") > 0) {
            throw new RuntimeException("url is unavailable,url=" + url);
        }
        // 地址
        properties.put(PropertyKeyConst.SERVER_ADDR, serverAddr);
        // username,password
        int propIndex = url.indexOf('?');
        if (propIndex > 0) {
            String[] sl = url.split("\\?");
            String strUrlParams = sl[1];
            Properties prop = new Properties();
            String[] params;
            if (strUrlParams.contains("&")) {
                params = strUrlParams.split("&");
            } else {
                params = new String[]{strUrlParams};
            }
            for (String p : params) {
                if(p.startsWith(PASSWORD_STR)){
                    String password = p.substring(PASSWORD_STR.length()+1);
                    prop.put(PASSWORD_STR, password);
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
            }
            properties.put(PropertyKeyConst.USERNAME, prop.getProperty(USER_STR));
            String pass = prop.getProperty(PASSWORD_STR);
            if (pass != null) {
                properties.put(PropertyKeyConst.PASSWORD, pass);
            }
        }
        // 命名空间
        properties.put(NAMESPACE_STR, getNacosNamespace(url));
        return properties;
    }

    public static String getNacosNamespace(String url) {
        int index = url.indexOf('/');
        if (index > 0) {
            String result = "";
            int endIndex = url.indexOf('?');
            if (endIndex > 0) {
                result = url.substring(index + 1, endIndex);
            } else {
                result = url.substring(index + 1);
            }
            if (result.endsWith("/")) {
                result = result.substring(0, result.length() - 1);
            }
            return result;
        } else {
            return "public";
        }
    }

}
