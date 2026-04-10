package com.hzk.webserver.tongweb;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TongwebBugTest {

    @Test
    public void convertTest() {
        PersonWrapper testDemo = new PersonWrapper();
        testDemo.setName("tes");
        List<Person> personList = new ArrayList<>();
        Person person = new Person();
        person.setId(10L);
        person.setName("test");
        personList.add(person);
        testDemo.setPersonList(personList);
        // 注册前
        List<Person> list1 = null;
        try {
            PersonWrapper copy = new PersonWrapper();
            org.apache.commons.beanutils.BeanUtils.copyProperties(copy, testDemo);
            list1 = copy.getPersonList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // tongweb的List.class注册逻辑
        ConvertUtils.register(new Converter() {
            public Object convert(Class type, Object value) {
                String valueString = String.valueOf(value);
                String[] strArr = valueString.split(",");
                List<String> list = Arrays.asList(strArr);
                return list;
            }
        }, List.class);

        // 注册后
        List<Person> list2 = null;
        try {
            PersonWrapper copy = new PersonWrapper();
            org.apache.commons.beanutils.BeanUtils.copyProperties(copy, testDemo);
            list2 = copy.getPersonList();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}


