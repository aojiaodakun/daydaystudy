package com.hzk.java.collection;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SetTest {

    @Test
    public void hashSetTest() throws Exception {
        Set<String> set = new HashSet<>(4);
        set.add("a");
        set.add("b");
        set.add("c");

        int size = set.size();
        System.out.println(size);

        HashSet<String> set1 = new HashSet<>(set);
        ArrayList<String> list = new ArrayList<>(set1);
        System.out.println(list);
    }

}
