package com.hzk.service.bootstrap;

import org.apache.dubbo.common.extension.SPI;

@SPI
public interface BootServer {
    void start(String[] args) throws Exception;
}
