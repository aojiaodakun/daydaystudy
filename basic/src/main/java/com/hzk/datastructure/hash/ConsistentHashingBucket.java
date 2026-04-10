package com.hzk.datastructure.hash;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 带虚拟节点的一致性Hash算法
 */
public class ConsistentHashingBucket {
    private static final String SPLIT = "&";
    private List<String> nodes;

    private SortedMap<Integer, String> virtualNodes = new TreeMap<>();

    private Map<String, String> vrMap = new ConcurrentHashMap<>();
    private static final int VIRTUAL_NODES = 128;


    public ConsistentHashingBucket(List<String> nodes) {
        this.nodes = Collections.unmodifiableList(nodes);
        for (String str : nodes) {
            for (int i = 0; i < VIRTUAL_NODES; i++) {
                String virtualNodeName = str + SPLIT + i;
                vrMap.put(virtualNodeName, str);
                int hash = getHash(virtualNodeName);
                virtualNodes.put(hash, virtualNodeName);
            }
        }
    }


    /**
     * 使用FNV1_32_HASH算法计算服务器的Hash值
     */
    private static int getHash(String str) {
        final int p = 16777619;
        int hash = (int) 2166136261L;
        for (int i = 0; i < str.length(); i++) {
            hash = (hash ^ str.charAt(i)) * p;
        }
        hash += hash << 13;
        hash ^= hash >> 7;
        hash += hash << 3;
        hash ^= hash >> 17;
        hash += hash << 5;

        if (hash < 0) {
            hash = Math.abs(hash);
        }
        return hash;
    }

    public String getBucket(String key) {
        int hash = getHash(key);
        SortedMap<Integer, String> subMap = virtualNodes.tailMap(hash);
        Integer i;
        if (subMap.isEmpty()) {
            i = virtualNodes.firstKey();
        } else {
            i = subMap.firstKey();
        }
        String virtualNode = virtualNodes.get(i);
        return vrMap.get(virtualNode);
    }

    public List<String> getBuckets() {
        return nodes;
    }

    public void addNode(String node) {
        for (int i = 0; i < VIRTUAL_NODES; i++) {
            String virtualNodeName = node + SPLIT + i;
            vrMap.put(virtualNodeName, node);
            int hash = getHash(virtualNodeName);
            virtualNodes.put(hash, virtualNodeName);
        }
        List<String> _nodes = new ArrayList<>();
        _nodes.addAll(nodes);
        _nodes.add(node);
        nodes = Collections.unmodifiableList(_nodes);
    }

    public void removeNode(String node) {
        for (int i = 0; i < VIRTUAL_NODES; i++) {
            String virtualNodeName = node + SPLIT + String.valueOf(i);
            vrMap.put(virtualNodeName, node);
            int hash = getHash(virtualNodeName);
            virtualNodes.remove(hash);
        }
        List<String> _nodes = new ArrayList<>();
        _nodes.addAll(nodes);
        _nodes.remove(node);
        nodes = Collections.unmodifiableList(_nodes);
    }

    public static void main(String[] args) throws Exception {
        List<String> list = new ArrayList<>();
        list.add("Node1");
        list.add("Node2");
        list.add("Node3");

        ConsistentHashingBucket ch = new ConsistentHashingBucket(list);
        // 测试数据分布
        Map<String, Integer> nodeCount = new HashMap<>();
        for (int i = 0; i < 10000; i++) {
            String key = "key" + i;
            String node = ch.getBucket(key);
            nodeCount.put(node, nodeCount.getOrDefault(node, 0) + 1);
        }
        // 打印数据分布
        System.out.println("数据分布：");
        nodeCount.forEach((node, count) -> {
            System.out.println(node + ": " + count + " 个数据");
        });

        // 移除一个节点
        System.out.println("\n移除 Node2 后：");
        ch.removeNode("Node2");
        nodeCount.clear();
        for (int i = 0; i < 10000; i++) {
            String key = "key" + i;
            String node = ch.getBucket(key);
            nodeCount.put(node, nodeCount.getOrDefault(node, 0) + 1);
        }
        nodeCount.forEach((node, count) -> {
            System.out.println(node + ": " + count + " 个数据");
        });

    }
}

