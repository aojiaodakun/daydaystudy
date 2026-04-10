package com.hzk.java.jmx.mbean.standard;

public interface HelloServiceMBean {

    void sayHello();
    int add(int x, int y);

    String getName();

    int getCacheSize();
    void setCacheSize(int size);

    void setName(String name);

    String getAge();

    void setAge(String age);

    void helloWorld();

    void helloWorld(String str);

    void getTelephone();
}
