package com.hzk.util;


import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigurationUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationUtil.class);
    private static String _get(String key, boolean includeEnv) {
        String v = System.getProperty(key);
        if (v == null && includeEnv) {
            v = System.getenv(key);
        }
        return v;
    }

    public static String getString(String key, String defaultValue, boolean includeEnv) {
        String v = _get(key, includeEnv);
        return v == null ? defaultValue : v;
    }

    public static String getString(String key, String defaultValue) {
        return getString(key, defaultValue, true);
    }

    public static String getString(String key) {
        return getString(key, null);
    }

    public static Integer getInteger(String key, Integer defaultValue, boolean includeEnv) {
        String v = _get(key, includeEnv);
        return v == null ? defaultValue : Integer.valueOf(v);
    }

    public static Integer getInteger(String key, Integer defaultValue) {
        return getInteger(key, defaultValue, true);
    }

    public static Integer getInteger(String key) {
        return getInteger(key, null);
    }

    public static Long getLong(String key, Long defaultValue, boolean includeEnv) {
        String v = _get(key, includeEnv);
        return v == null ? defaultValue : Long.valueOf(v);
    }

    public static Long getLong(String key, Long defaultValue) {
        return getLong(key, defaultValue, true);
    }

    public static Long getLong(String key) {
        return getLong(key, null);
    }

    public static Boolean getBoolean(String key, Boolean defaultValue, boolean includeEnv) {
        String v = _get(key, includeEnv);
        return v == null ? defaultValue : Boolean.valueOf(v);
    }

    public static Boolean getBoolean(String key, Boolean defaultValue) {
        return getBoolean(key, defaultValue, true);
    }

    //读取租户级配置
    public static Boolean getBoolean(String key, Boolean defaultValue, String tenantId) {
        Boolean returnValue = defaultValue;//默认为缺省值
        String keyValue = System.getProperty(tenantId + "_" + key);
        if (StringUtils.isEmpty(keyValue)) {
            keyValue = System.getProperty(key);
        }
        if (StringUtils.isNotEmpty(keyValue) && "true".equalsIgnoreCase(keyValue.trim())) {
            returnValue = Boolean.TRUE;
        }
        if (StringUtils.isNotEmpty(keyValue) && "false".equalsIgnoreCase(keyValue.trim())) {
            returnValue = Boolean.FALSE;
        }
        return returnValue;
    }

    public static Boolean getBoolean(String key) {
        return getBoolean(key, null);
    }

}
