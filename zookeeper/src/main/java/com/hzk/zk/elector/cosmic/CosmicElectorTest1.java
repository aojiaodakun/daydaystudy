package com.hzk.zk.elector.cosmic;

public class CosmicElectorTest1 {

    public static void main(String[] args) throws Exception{

        Elector elector = ElectFactory.getElector("hzkCluster");
        elector.registerListener(new ElectorListener() {
            @Override
            public void notifyLostMaster() {
                System.err.println("notifyLostMaster");
            }

            @Override
            public void notifyMaster() {
                System.err.println("notifyMaster");
            }
        });
        elector.start();


        System.in.read();
    }

}
