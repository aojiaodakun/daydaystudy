package com.hzk.java.lock;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReentrantReadWriteLockTest {

    static Map<String,Integer> map = new HashMap<>();

    static ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public static void main(String[] args) {


    }

    void put(String key,Integer value){
        try {
            lock.writeLock().lock();
        }finally {

        }
    }


}
