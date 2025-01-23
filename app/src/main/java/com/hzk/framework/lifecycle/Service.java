package com.hzk.framework.lifecycle;

public interface Service {
    String getName();

    void start() throws InterruptedException;

    void stop();

    boolean isStarted();
}
