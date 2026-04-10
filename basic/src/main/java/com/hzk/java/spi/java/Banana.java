package com.hzk.java.spi.java;

public class Banana implements Fruit {
    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }
}
