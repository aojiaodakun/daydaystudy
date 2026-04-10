package com.hzk.safe.payload.serializable.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;

/**
 * 不走多态类型解析
 * | JSON 结构 | 结果              |
 * | ------- | --------------- |
 * | `{}`    | `LinkedHashMap` |
 * | `[]`    | `ArrayList`     |
 * | `"x"`   | `String`        |
 * | `1`     | `Integer`       |
 */
public class JacksonTest {

    public static void main(String[] args) throws Exception {
        // enableDefaultTyping
        enableDefaultTyping();
        // activateDefaultTyping
        activateDefaultTyping();
        // @JsonTypeInfo(use = Id.CLASS / MINIMAL_CLASS)
        // 注解级多态
        jsonTypeInfo();
    }


    private static void enableDefaultTyping() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        // 多态反序列化，即支持任意类实例化
        objectMapper.enableDefaultTyping();
        String maliciousJson = "[\"com.hzk.safe.payload.serializable.jackson.JacksonTest$AttackClass\", {}]";
        // 如果没有禁用类型处理，这里可能会引发 RCE
        Object obj = objectMapper.readValue(maliciousJson, Object.class);
        System.out.println(obj);
    }

    private static void activateDefaultTyping() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.activateDefaultTyping(
                LaissezFaireSubTypeValidator.instance,//我什么类型都允许，等同于enableDefaultTyping()
                ObjectMapper.DefaultTyping.NON_FINAL
        );
        // 很多开发以为这样是“官方推荐”
//        objectMapper.activateDefaultTyping(
//                BasicPolymorphicTypeValidator.builder().build(),
//                ObjectMapper.DefaultTyping.NON_FINAL
//        );

        String maliciousJson = "[\"com.hzk.safe.payload.serializable.jackson.JacksonTest$AttackClass\", {}]";
        // 如果没有禁用类型处理，这里可能会引发 RCE
        Object obj = objectMapper.readValue(maliciousJson, Object.class);
        System.out.println(obj);
    }

    public static class AttackClass {
        public AttackClass() {
            System.out.println("AttackClass init");
        }
    }


    private static void jsonTypeInfo() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = "{\n" +
                "          \"data\": {\n" +
                "            \"@class\": \"com.hzk.safe.payload.serializable.jackson.Evil\",\n" +
                "            \"msg\": \"hello jackson\"\n" +
                "          }\n" +
                "        }";
        Wrapper w = objectMapper.readValue(json, Wrapper.class);
        // 实例化了Evil
        System.out.println(w);
        System.out.println("data runtime class = " + w.data.getClass());
        System.out.println("data value        = " + w.data);
    }

}
