package com.hzk.spi.dubbo;

import org.apache.dubbo.common.extension.SPI;

@SPI("byd")
public class BydCar implements Car {
    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }
}
