package com.hzk.spi.dubbo;

import org.apache.dubbo.common.extension.ExtensionLoader;

/**
 * https://cloud.tencent.com/developer/article/1685613
 * 资源文件位置：/META-INF/dubbo/com.hzk.spi.dubbo.Car
 */
public class DubboSpiTest {

    public static void main(String[] args) {
        ExtensionLoader<Car> extensionLoader = ExtensionLoader.getExtensionLoader(Car.class);
        Car bydCar = extensionLoader.getExtension("byd");
        System.out.println(bydCar.getName());
        Car xiaomiCar = extensionLoader.getExtension("xiaomi");
        System.out.println(xiaomiCar.getName());
    }

}
