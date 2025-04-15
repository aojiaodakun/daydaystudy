package com.hzk.util;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 带虚拟节点的一致性Hash算法
 */
public class ConsistentHashingBucket<N> {

    private static final String SPLIT = "&";
    // 真实节点
    private List<N> nodeList;
    // 虚拟节点数
    public static final int VIRTUAL_NODES = 128;
    // Map<虚拟节点hash, 真实节点>
    private final SortedMap<Long, N> virtualNodes = new TreeMap<>();

    public ConsistentHashingBucket(List<N> nodeList) {
        this.nodeList = Collections.unmodifiableList(nodeList);
        for (N node : nodeList) {
            for (int i = 0; i < VIRTUAL_NODES; i++) {
                String virtualNodeName = node.toString() + SPLIT + i;
                long hash = hash(virtualNodeName);
                virtualNodes.put(hash, node);
            }
        }
    }

    public List<N> getBucketList() {
        return nodeList;
    }

    public void addNode(N node) {
        for (int i = 0; i < VIRTUAL_NODES; i++) {
            String virtualNodeName = node.toString() + SPLIT + i;
            long hash = hash(virtualNodeName);
            virtualNodes.put(hash, node);
        }
        List<N> _nodes = new ArrayList<>();
        _nodes.addAll(nodeList);
        _nodes.add(node);
        nodeList = Collections.unmodifiableList(_nodes);
    }

    public void removeNode(N node) {
        for (int i = 0; i < VIRTUAL_NODES; i++) {
            String virtualNodeName = node.toString() + SPLIT + i;
            long hash = hash(virtualNodeName);
            virtualNodes.remove(hash);
        }
        List<N> _nodes = new ArrayList<>();
        _nodes.addAll(nodeList);
        _nodes.remove(node);
        nodeList = Collections.unmodifiableList(_nodes);
    }

    public List<N> getBucket(String key, int nodeCount) {
        List<N> result = new ArrayList<>(nodeCount);
        if (virtualNodes.isEmpty()) {
            return result;
        }
        // 真实节点数 < nodes.size()
        if (nodeList.size() <= nodeCount) {
            return nodeList;
        }
        long hash = hash(key);
        SortedMap<Long, N> tailMap = virtualNodes.tailMap(hash);
        // 使用Set去重，确保返回的节点不重复
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
}

