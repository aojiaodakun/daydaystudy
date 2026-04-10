package com.hzk.safe.payload.serializable.common.dto;

import java.io.Serializable;
import java.util.Map;

public class MserviceRequestDTO implements Serializable {

    private String name;

    private int age;

    private long money;

    private Map<String, Object> paramMap;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public long getMoney() {
        return money;
    }

    public void setMoney(long money) {
        this.money = money;
    }

    public void print(String name) {
        System.out.println(name);
    }

    public Map<String, Object> getParamMap() {
        return paramMap;
    }

    public void setParamMap(Map<String, Object> paramMap) {
        this.paramMap = paramMap;
    }

    @Override
    public String toString() {
        return "MserviceRequestDTO{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", money=" + money +
                ", paramMap=" + paramMap +
                '}';
    }
}
