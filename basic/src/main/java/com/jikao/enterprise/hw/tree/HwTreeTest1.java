package com.jikao.enterprise.hw.tree;

import java.util.*;

/**
 * 8.二叉树（2题）
 * (1) *leetcode 剑指offer 32 — II.从上到下打印二叉树 II
 * (2) leetcode 剑指offer 32 — III.从上到下打印二叉树 III
 */
public class HwTreeTest1 {

    public static void main(String[] args) throws Exception {
//        test1();
        test2();
    }

    /**
     * (1) *leetcode 剑指offer 32 — II.从上到下打印二叉树 II
     * [
     *   [3],
     *   [9,20],
     *   [15,7]
     * ]
     */
    private static void test1() {
        TreeNode rootNode = new TreeNode(3);

        TreeNode node9 = new TreeNode(9);
        TreeNode node20 = new TreeNode(20);
        TreeNode node15 = new TreeNode(15);
        TreeNode node7 = new TreeNode(7);

        rootNode.left = node9;
        rootNode.right = node20;

        node20.left = node15;
        node20.right = node7;

        List<List<Integer>> lists = levelOrder(rootNode);
        System.out.println(lists);
    }

    public static List<List<Integer>> levelOrder(TreeNode root) {
        Queue<TreeNode> queue = new LinkedList<>();
        List<List<Integer>> res = new ArrayList<>();
        if (root != null) {
            queue.add(root);
        }
        while (!queue.isEmpty()) {
            List<Integer> tmp = new ArrayList<>();
            for (int i = queue.size(); i > 0; i--) {
                TreeNode node = queue.poll();
                tmp.add(node.val);
                if (node.left != null) queue.add(node.left);
                if (node.right != null) queue.add(node.right);
            }
            res.add(tmp);
        }
        return res;
    }


    /**
     * (2) leetcode 剑指offer 32 — III.从上到下打印二叉树 III
     * [
     *   [3],
     *   [20,9],
     *   [15,7]
     * ]
     */
    private static void test2() {
        TreeNode rootNode = new TreeNode(3);

        TreeNode node9 = new TreeNode(9);
        TreeNode node20 = new TreeNode(20);
        TreeNode node15 = new TreeNode(15);
        TreeNode node7 = new TreeNode(7);

        rootNode.left = node9;
        rootNode.right = node20;

        node20.left = node15;
        node20.right = node7;

        List<List<Integer>> lists = levelOrder2(rootNode);
        System.out.println(lists);
    }

    public static List<List<Integer>> levelOrder2(TreeNode root) {
        Queue<TreeNode> queue = new LinkedList<>();
        List<List<Integer>> res = new ArrayList<>();
        if (root != null) queue.add(root);
        while (!queue.isEmpty()) {
            LinkedList<Integer> tmp = new LinkedList<>();
            for (int i = queue.size(); i > 0; i--) {
                TreeNode node = queue.poll();
                if (res.size() % 2 == 0){
                    tmp.addLast(node.val); // 偶数层 -> 队列头部
                } else {
                    tmp.addFirst(node.val); // 奇数层 -> 队列尾部
                }
                if (node.left != null) queue.add(node.left);
                if (node.right != null) queue.add(node.right);
            }
            res.add(tmp);
        }
        return res;
    }

}
class TreeNode {
     int val;
     TreeNode left;
     TreeNode right;
     TreeNode(int x) { val = x; }

    @Override
    public String toString() {
        return String.valueOf(val);
    }
}