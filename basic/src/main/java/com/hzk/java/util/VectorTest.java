package com.hzk.java.util;

import java.util.Vector;

public class VectorTest {

    public static void main(String[] args) throws Exception {
        Vector<String> vector = new Vector<>();
        vector.add("a");
        vector.add("b");
        vector.add("c");

        String s = vector.get(0);
        System.out.println();
    }

}
