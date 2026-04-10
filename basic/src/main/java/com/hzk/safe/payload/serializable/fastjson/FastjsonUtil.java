package com.hzk.safe.payload.serializable.fastjson;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

public final class FastjsonUtil {
    public static String toJSONString(Object obj) {
        return JSON.toJSONString(obj, SerializerFeature.UseSingleQuotes);
    }

    public static JSONObject parseObject(String text) {
        return JSON.parseObject(text);
    }

    public static JSONArray parseArray(String text) {
        return JSON.parseArray(text);
    }

    public static <T> T parseObject(String text, Class<T> cls) {
        return JSON.parseObject(text, cls);
    }
}
