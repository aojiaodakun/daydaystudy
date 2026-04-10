package com.hzk.test;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public class BitSetTest {

    public static void main(String[] args) {
        BitSet bitSet = new BitSet();
        bitSet.set(5);
        bitSet.set(5);
        bitSet.set(5);
        bitSet.set(4);
        bitSet.set(1);
        bitSet.set(6);
        List<Integer> list = snapshotActiveIndices(bitSet);
        System.out.println(list);
        bitSet.clear(5);
        System.out.println(list);


    }


    private static List<Integer> snapshotActiveIndices(BitSet bitSet) {
        List<Integer> list = new ArrayList<>();
        synchronized (bitSet) {
            for (int i = bitSet.nextSetBit(0); i >= 0; i = bitSet.nextSetBit(i + 1)) {
                list.add(i);
            }
        }
        return list;
    }

}
