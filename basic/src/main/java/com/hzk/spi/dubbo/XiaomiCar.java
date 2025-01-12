package com.hzk.spi.dubbo;

import org.apache.dubbo.common.extension.SPI;

@SPI("xiaomi")
public class XiaomiCar implements Car {
    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }
}
