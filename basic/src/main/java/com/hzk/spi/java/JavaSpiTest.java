package com.hzk.spi.java;

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * https://blog.csdn.net/m0_50542823/article/details/135361938
 * 资源文件位置：/META-INF/services/com.hzk.spi.java.Fruit
 */
public class JavaSpiTest {

    public static void main(String[] args) {
        ServiceLoader<Fruit> serviceLoader = ServiceLoader.load(Fruit.class);
        Iterator<Fruit> iterator = serviceLoader.iterator();
        while (iterator.hasNext()) {
            Fruit tempFruit = iterator.next();
            System.out.println(tempFruit.getName());
        }

    }


}
