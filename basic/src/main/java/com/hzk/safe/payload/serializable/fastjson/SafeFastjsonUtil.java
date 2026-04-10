package com.hzk.safe.payload.serializable.fastjson;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;

public final class SafeFastjsonUtil {

    /**
     * 全局 ParserConfig（安全配置）
     */
    private static final ParserConfig SAFE_CONFIG;

    static {
        SAFE_CONFIG = new ParserConfig();

        // 1️⃣ 彻底关闭 AutoType（核心）
        SAFE_CONFIG.setAutoTypeSupport(false);

        // 2️⃣ 开启安全模式（1.2.68+ 支持）
        SAFE_CONFIG.setSafeMode(true);

        // ❌ 不要 addAccept
        // ❌ 不要 addDeny（黑名单不可控）
    }

    private SafeFastjsonUtil() {
    }

    /**
     * 序列化：安全（无 RCE 风险）
     */
    public static String toJSONString(Object obj) {
        return JSON.toJSONString(
                obj,
                SerializerFeature.UseSingleQuotes,
                SerializerFeature.DisableCircularReferenceDetect
        );
    }

    /**
     * ❌ 不推荐：泛型 JSONObject 解析（仅限内部可信数据）
     */
    @Deprecated
    public static JSONObject parseObjectUnsafe(String text) {
        return JSON.parseObject(text);
    }

    /**
     * ❌ 不推荐：泛型 JSONArray 解析（仅限内部可信数据）
     */
    @Deprecated
    public static JSONArray parseArrayUnsafe(String text) {
        return JSON.parseArray(text);
    }

    /**
     * ✅ 推荐：安全反序列化（强类型）
     */
    public static <T> T parseObject(String text, Class<T> cls) {
        if (text == null || cls == null) {
            return null;
        }

        return JSON.parseObject(
                text,
                cls,
                SAFE_CONFIG,
                // 禁止任何 autoType 行为
                Feature.DisableSpecialKeyDetect
        );
    }
}

