package com.hzk.zk.elector.cosmic;


public interface ElectorListener {
    void notifyMaster();

    void notifyLostMaster();

}
