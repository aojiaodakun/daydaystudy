package com.hzk.spi.java;

public class Banana implements Fruit {
    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }
}
