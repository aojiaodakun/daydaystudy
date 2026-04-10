package com.hzk.java.jvm.gc;

/**
 * -Xms10m -Xmx10m -XX:+PrintGCDetails -XX:+UseSerialGC -XX:SurvivorRatio=8
 */
public class JvmGCTest {

    public static void main(String[] args) throws Exception {
        System.out.println("gc test");
    }

}
