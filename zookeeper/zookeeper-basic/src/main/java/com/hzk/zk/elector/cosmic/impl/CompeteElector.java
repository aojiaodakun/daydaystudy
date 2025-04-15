package com.hzk.zk.elector.cosmic.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import com.hzk.zk.elector.cosmic.Elector;
import com.hzk.zk.elector.cosmic.ElectorListener;

public abstract class CompeteElector implements Elector {
    private String electClusterName = "dts";

    private int heartbeatIntervalSeconds = 3;
    private int heartbeatTimeOutSeconds = heartbeatIntervalSeconds * 3 + 1;

    private Map<String, ElectorListener> listeners = new ConcurrentHashMap<>(1);

    private AtomicBoolean isStarted = new AtomicBoolean();
    protected final String masterKey;

    public CompeteElector(String systemName) {
        this.electClusterName = systemName;
        this.masterKey = getMasterKey(systemName);
    }

    public CompeteElector(String systemName, int heartbeatIntervalSeconds) {
        this.electClusterName = systemName;
        this.setHeartbeatIntervalSeconds(heartbeatIntervalSeconds);
        this.masterKey = getMasterKey(systemName);
    }

    private String getMasterKey(String systemName) {
        return "defaultCluster" + "_" + systemName + "_master" + System.getProperty("elect.Region", "");
    }

    public String getElectClusterName() {
        return electClusterName;
    }

    public int getHeartbeatTimeOutSeconds() {
        return heartbeatTimeOutSeconds;
    }

    public void setHeartbeatTimeOutSeconds(int heartbeatTimeOutSeconds) {
        this.heartbeatTimeOutSeconds = heartbeatTimeOutSeconds;
    }

    public int getHeartbeatIntervalSeconds() {
        return heartbeatIntervalSeconds;
    }

    public final void setHeartbeatIntervalSeconds(int heartbeatIntervalSeconds) {
        this.heartbeatIntervalSeconds = heartbeatIntervalSeconds;
    }

    @Override
    public void registerListener(ElectorListener listener) {
        String key = listener.getClass().getName();
        listeners.computeIfAbsent(key, k -> {
            return listener;
        });
    }

    @Override
    public void unRegisterListener(ElectorListener listener) {
        String key = listener.getClass().getName();
        listeners.remove(key);
    }

    protected void notifyMaster() {
        listeners.forEach((k, listener) -> {
            listener.notifyMaster();
        });
    }

    protected void notifyLostMaster() {
        listeners.forEach((k, listener) -> {
            listener.notifyLostMaster();
        });
    }

    @Override
    public synchronized void start() {
        if (!isStarted.get()) {
            doStart();
            isStarted.compareAndSet(false, true);
        }
    }

    protected abstract void doStart();

    protected void checkStarted() {
        if (!isStarted.get()) {
            throw new RuntimeException(getElectClusterName() + " ElectErrorCode.notStartedError");
        }
    }

}
