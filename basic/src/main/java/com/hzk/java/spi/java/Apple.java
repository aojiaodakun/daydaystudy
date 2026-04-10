package com.hzk.java.spi.java;

public class Apple implements Fruit {
    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }
}
