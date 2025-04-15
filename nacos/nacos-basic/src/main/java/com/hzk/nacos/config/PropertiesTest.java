package com.hzk.nacos.config;

import com.alibaba.nacos.api.PropertyKeyConst;

import java.util.Properties;

public class PropertiesTest {

    private static final String NAMESPACE_STR = "namespace";
    private static final String USER_STR = "user";
    private static final String PASSWORD_STR = "password";

    public static void main(String[] args) {
        String url = "localhost:8848";
        url = "localhost:8848/sit/";
        url = "localhost:8848/sit";
        url = "localhost:8848/sit?user=1&password=2";
        url = "localhost:8848?user=1&password=2";// zk也不支持此格式
//        String namespace = getNamespace(url);
//        namespace = getNamespace("localhost:8848/sit?user=1&password=2");

        Properties nacosProperties = getNacosProperties(url);
        System.out.println(nacosProperties);
    }

    private static Properties getNacosProperties(String url) {
        Properties properties = new Properties();
        String serverAddr = url.split("/")[0];
        if (serverAddr.indexOf("?") > 0) {
            throw new IllegalArgumentException("url is unavailable,url=" + url);
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
        properties.put(NAMESPACE_STR, getNamespace(url));
        return properties;
    }

    private static String getNamespace(String url) {
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
