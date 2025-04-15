package com.hzk.zk.elector.cosmic;

public interface Elector {
    void start();

    boolean isMaster();

    Node getMaster();

    void registerListener(ElectorListener listener);

    void unRegisterListener(ElectorListener listener);
}
