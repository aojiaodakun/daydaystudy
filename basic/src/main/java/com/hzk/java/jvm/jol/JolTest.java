package com.hzk.java.jvm.jol;

import org.openjdk.jol.info.ClassLayout;

public class JolTest {

    public static void main(String[] args) {
        Object o = new Object();
        System.out.println("hashCode=" + o.hashCode()+",十六进制:" + Integer.toHexString(o.hashCode()));
        System.out.println(ClassLayout.parseInstance(o).toPrintable());
    }

}
