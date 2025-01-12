package com.hzk.spi.java;

public class Apple implements Fruit {
    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }
}
