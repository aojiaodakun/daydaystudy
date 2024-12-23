package com.hzk.collection;

import org.junit.Test;

import java.util.*;

/**
 * 1、了解Map接口的常用api
 * 2、了解Map多种实现类的底层实现差异，以及LinkedHashMap的拓展应用
 */
public class MapTest {

    @Test
    public void hashMapTest() throws Exception {
        Map<String, String> map = new HashMap<>();
        // put
        map.put("a", "1");
        map.put("b", "2");
        map.put("c", "3");
        System.out.println(map);
        map.get("c");
        System.out.println("----------------");
        // containsKey
        boolean containsKey = map.containsKey("c");
        System.out.println(containsKey);
        System.out.println("----------------");
        // putIfAbsent
        map.putIfAbsent("c", "3");
        System.out.println(map);
        System.out.println("----------------");
        // remove
        String removeValue = map.remove("c");
        System.out.println("removeValue:" + removeValue);
        System.out.println("----------------");

        /**
         * 列出多种遍历map的方式
         */
//        java常用方式
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            System.out.println("Key:" + key + ",Value:" + value);
        }
//   Iterator
        Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            String key = entry.getKey();
            String value = entry.getValue();
            System.out.println("KeyI:" + key + ",ValueI:" + value);
        }



    }

    /**
     * 使用LinkedHashMap实现LRU淘汰算法
     * 参考
     * https://blog.csdn.net/qq_33697094/article/details/121035338
     */
    @Test
    public void linkedHashMapTest() throws Exception {
        LRUCache<String,String> lruCache =new LRUCache<>(2);
        lruCache.put("a", "1");
        lruCache.put("b", "2");
        System.out.println(lruCache);
        lruCache.put("c", "3");
        System.out.println(lruCache);
        lruCache.get("b");
        System.out.println(lruCache);
        lruCache.put("d","4");
        System.out.println(lruCache);
    }

    public class LRUCache<K, V> extends LinkedHashMap<K, V> {
            private int cap;
            private static final long serialVersionUID = 1L;
            public LRUCache(int cap) {
                super(16, 0.75f, true);
                this.cap = cap;
            }
            @Override
            protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
                return size() > cap;
            }
    }





    /**
     * treeMap实现了SortedMap接口，key自动排序
     * 了解即可
     */
    @Test
    public void treeMapTest() throws Exception {
        Map<String, String> map = new TreeMap<>();
        // 无序put
        map.put("b", "2");
        map.put("a", "1");
        map.put("d", "4");
        map.put("c", "3");
        System.out.println(map);
    }

}
