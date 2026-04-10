package com.hzk.datastructure.hash;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 带虚拟节点的一致性Hash算法
 */
public class ConsistentHash<N> {

    private static final String SPLIT = "&";
    // 真实节点
    private List<N> nodes;
    // 虚拟节点数
    public static final int VIRTUAL_NODES = 128;
    // Map<虚拟节点hash, 真实节点>
    private final SortedMap<Long, N> virtualNodes = new TreeMap<>();

    public ConsistentHash(List<N> nodes, int nodeCount) {
        this.nodes = Collections.unmodifiableList(nodes);
        for (N node : nodes) {
            for (int i = 0; i < VIRTUAL_NODES; i++) {
                String virtualNodeName = node.toString() + SPLIT + i;
                long hash = hash(virtualNodeName);
                virtualNodes.put(hash, node);
            }
        }
    }

    public void addNode(N node) {
        for (int i = 0; i < VIRTUAL_NODES; i++) {
            String virtualNodeName = node.toString() + SPLIT + i;
            long hash = hash(virtualNodeName);
            virtualNodes.put(hash, node);
        }
        List<N> _nodes = new ArrayList<>();
        _nodes.addAll(nodes);
        _nodes.add(node);
        nodes = Collections.unmodifiableList(_nodes);
    }

    public void removeNode(N node) {
        for (int i = 0; i < VIRTUAL_NODES; i++) {
            String virtualNodeName = node.toString() + SPLIT + i;
            long hash = hash(virtualNodeName);
            virtualNodes.remove(hash);
        }
        List<N> _nodes = new ArrayList<>();
        _nodes.addAll(nodes);
        _nodes.remove(node);
        nodes = Collections.unmodifiableList(_nodes);
    }

    public List<N> getBucket(String key, int nodeCount) {
        List<N> result = new ArrayList<>(nodeCount);
        if (virtualNodes.isEmpty()) {
            return result;
        }
        // 真实节点数 < nodes.size()
        if (nodes.size() <= nodeCount) {
            return nodes;
        }
        long hash = hash(key);
        SortedMap<Long, N> tailMap = virtualNodes.tailMap(hash);

        // 使用TreeSet去重，确保返回的节点不重复
        Set<N> nodeSet = new LinkedHashSet<>();
        Iterator<Long> it = tailMap.keySet().iterator();
        while (it.hasNext() && nodeSet.size() < nodeCount) {
            nodeSet.add(tailMap.get(it.next()));
        }

        // 如果tailMap中节点不足，从头开始取
        it = virtualNodes.keySet().iterator();
        while (it.hasNext() && nodeSet.size() < nodeCount) {
            nodeSet.add(virtualNodes.get(it.next()));
        }
        result.addAll(nodeSet);
        return result;
    }

    private long hash(String key) {
        final int p = 16777619;
        int hash = (int) 2166136261L;
        for (int i = 0; i < key.length(); i++) {
            hash = (hash ^ key.charAt(i)) * p;
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

    public static void main(String[] args) {
        List<String> nodes = Arrays.asList(
                "192.168.1.1",
                "192.168.1.2",
                "192.168.1.3",
                "192.168.1.4"
        );
        // 每个key映射到2个节点
        int nodeCount = 1;
        ConsistentHash<String> consistentHash = new ConsistentHash<>(nodes, nodeCount);
        /**
         * 单数据
         */
        String testKey = "user_12345";
        List<String> selectedNodeList = consistentHash.getBucket(testKey, nodeCount);
        System.out.println("Key '" + testKey + "' 映射到节点: " + selectedNodeList);

        /**
         * 测试数据分布
         */
        Map<String, Integer> nodeCountMap = new HashMap<>();
        for (int i = 0; i < 10000; i++) {
            String key = "key" + i;
            List<String> tempSelectedNodeList = consistentHash.getBucket(key, nodeCount);
            for(String tempNode : tempSelectedNodeList) {
                nodeCountMap.put(tempNode, nodeCountMap.getOrDefault(tempNode, 0) + 1);
            }
        }
        // 打印数据分布
        System.out.println("数据分布：");
        nodeCountMap.forEach((node, count) -> {
            System.out.println(node + ": " + count + " 个数据");
        });


    }
}