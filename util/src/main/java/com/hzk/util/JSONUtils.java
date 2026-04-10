package com.hzk.util;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * json处理工具类
 */
public class JSONUtils {

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final ObjectMapper mapperIndent = new ObjectMapper();
    private static final ObjectMapper mapperIgnore = new ObjectMapper();

    /**
     * 将对象转为String文本
     *
     * @param source 需要转换的对象
     * @return String文本
     * @throws IOException
     */
    public static String toString(Object source) throws IOException {
        return toString(source, false);
    }

    /**
     * 将对象转换为JSON字符串
     *
     * @param source 待转换字符串
     * @param indent 生成字符串是否需要缩进
     * @return JSON字符串
     */
    public static String toString(Object source, boolean indent) throws IOException {
        try {
            if (indent) {
                mapperIndent.configure(SerializationFeature.INDENT_OUTPUT, true);
                return mapperIndent.writeValueAsString(source);
            }
            String result = mapper.writeValueAsString(source);
            return result;
        } catch (IOException e) {
            //logger.error("cast source object toString error", e);
            throw e;
        }
    }

    /**
     * JSON字符串转换成目标对象
     *
     * @param source 转换JSON字符串
     * @param type   转换结果类型
     * @return 如果转换成功返回type指定的对象，失败返回null
     * @throws Exception
     */
    public static <T> T cast(String source, Class<T> type) throws IOException {
        return cast(source, type, false);
    }

    /**
     * JSON字符串转换成目标对象
     *
     * @param source            转换JSON字符串
     * @param type              转换结果类型
     * @param ignoreUnkownField 当反序列化json时，未知属性会引起的反序列化被打断,
     *                          如果为true， 目标类中将忽略source中多余的字段
     *                          如果为false, 转换失败将中断
     * @return 如果转换成功返回type指定的对象，失败返回null
     * @throws Exception
     */
    public static <T> T cast(String source, Class<T> type, boolean ignoreUnkownField) throws IOException {
        try {
            if (ignoreUnkownField) {
                mapperIgnore.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
                return mapperIgnore.readValue(source, type);

            }
            T result = mapper.readValue(source, type);
            return result;
        } catch (IOException e) {
            //logger.error("cast object from source error", e);
            throw e;
        }
    }

    /**
     * JSON字符串转换成复杂目标对象
     *
     * @param source         转换JSON字符串
     * @param collectionType 待转换复杂集合类型, 如ArrayList, HashMap
     * @param elementType    集合包含元素类型
     * @return 如果转换成功返回type指定的对象，失败返回null
     * @throws IOException
     */
    public static <T> T cast(String source, Class<?> collectionType, Class<?>... elementType) throws IOException {
        return cast(source, true, collectionType, elementType);
    }

    /**
     * JSON字符串转换成复杂目标对象
     *
     * @param source            转换JSON字符串
     * @param ignoreUnkownField 当反序列化json时，未知属性会引起的反序列化被打断,
     *                          如果为true， 目标类中将忽略source中多余的字段
     *                          如果为false, 转换失败将中断
     * @param collectionType    待转换复杂集合类型, 如ArrayList, HashMap
     * @param elementType       集合包含元素类型
     * @return 如果转换成功返回type指定的对象，失败返回null
     * @throws IOException
     */
    public static <T> T cast(String source, boolean ignoreUnkownField, Class<?> collectionType, Class<?>... elementType) throws IOException {
        try {
            if (ignoreUnkownField) {
//                mapper.disable(org.codehaus.jackson.map.DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES);
                mapperIgnore.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
                JavaType type = getCollectionType(collectionType, elementType);
                T result = mapperIgnore.readValue(source, type);
                return result;
            }
            JavaType type = getCollectionType(collectionType, elementType);
            T result = mapper.readValue(source, type);
            return result;
        } catch (IOException e) {
            //logger.error("cast object from source error", e);
            throw e;
        }
    }

    /**
     * 获取数据类型
     *
     * @param collectionClass 待转换复杂集合类型, 如ArrayList, HashMap
     * @param elementClasses  集合包含元素类型
     * @return 数据类型结果
     */
    public static JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
        return mapper.getTypeFactory()
                .constructParametricType(collectionClass, elementClasses);
    }

    /**
     * 返回"{}"
     *
     * @return "{}"
     */
    public static String getEmpty() {
        return "{}";
    }

    /**
     * @param sHttpResponse
     * @return
     * @throws Exception
     * @method nullDataToEmptyList
     * @description 转换响应信息中的data参数，防止出现转换成list时报错
     */
    @SuppressWarnings("unchecked")
    public static String nullDataToEmptyList(String sHttpResponse)
            throws Exception {
        if (StringUtils.isBlank(sHttpResponse)) {
            return sHttpResponse;
        }
        Map<String, Object> resMap = JSONUtils.cast(sHttpResponse,
                HashMap.class, true);
        if (resMap != null) {
            if (StringUtils.isBlank((String) resMap.get("data"))) {
                resMap.put("data", new ArrayList<>());
                return JSONUtils.toString(resMap);
            }
        }
        return sHttpResponse;
    }
}

