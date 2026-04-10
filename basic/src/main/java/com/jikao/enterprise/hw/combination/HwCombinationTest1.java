package com.jikao.enterprise.hw.combination;

import java.util.*;

/**
 * 5.排列组合（2题）
 * (1) *leetcode 面试题08.08.有重复字符串的排列组合
 * (2) leetcode 77.组合
 */
public class HwCombinationTest1 {

    List<String> list0 = new ArrayList<>();
    List<String> list1 = new LinkedList<>();

    public static void main(String[] args) throws Exception {
        HwCombinationTest1 my = new HwCombinationTest1();
        my.test0();
//        my.test1();
//        my.test2();


    }

    List<String> res = new ArrayList<>();
    /**
     * 面试题 08.07. 无重复字符串的排列组合
     *
     *  输入：S = "qwe"
     *  输出：["qwe", "qew", "wqe", "weq", "ewq", "eqw"]
     *
     *  输入：S = "ab"
     *  输出：["ab", "ba"]
     *
     * @throws Exception
     */
    private void test0() throws Exception{
        String[] result = permutation0("qwe");
        for (int i = 0; i < result.length; i++) {
            System.out.println(result[i]);
        }
    }

    public String[] permutation0(String s) {
        boolean[] isVisited = new boolean[s.length()];
        trackback(s,isVisited,new StringBuilder());
        String[] strs = new String[res.size()];
        for(int i=0;i<res.size();i++){
            strs[i] = res.get(i);
        }
        return strs;
    }

    public void trackback(String s,boolean[] isVisited,StringBuilder sb){
        if(sb.length()==s.length()){
            res.add(sb.toString());
            return;
        }
        for(int i=0;i<s.length();i++){
            if(!isVisited[i]){
                sb.append(s.charAt(i));
                isVisited[i]=true;
                trackback(s,isVisited,sb);
                sb.deleteCharAt(sb.length()-1);
                isVisited[i] = false;
            }
        }
    }



    List<String> permutations = new ArrayList<String>();
    StringBuffer temp = new StringBuffer();
    char[] arr;
    int n;
    boolean[] visited;
    /**
     * (1) *leetcode 面试题08.08.有重复字符串的排列组合
     * 有重复字符串的排列组合。编写一种方法，计算某字符串的所有排列组合。
     *  输入：S = "qqe"
     *  输出：["eqq","qeq","qqe"]
     *
     *  输入：S = "ab"
     *  输出：["ab", "ba"]
     *
     *  提示:
     * 字符都是英文字母。
     * 字符串长度在[1, 9]之间。
     * @throws Exception
     */
    private void test1() throws Exception{
        String str = "abc";
        String[] permutation = permutation(str);
        System.out.println(permutation);
    }

    public String[] permutation(String s) {
        arr = s.toCharArray();
        Arrays.sort(arr);
        this.n = s.length();
        this.visited = new boolean[n];
        backtrack1(0);
        return permutations.toArray(new String[permutations.size()]);
    }

    public void backtrack1(int index) {
        if (index == n) {
            permutations.add(temp.toString());
        } else {
            for (int i = 0; i < n; i++) {
                // 排序之后，相同字符位于字符数组中的相邻位置，可以利用这一特点去重
                if (visited[i] || (i > 0 && arr[i] == arr[i - 1] && !visited[i - 1])) {
                    continue;
                }
                temp.append(arr[i]);
                visited[i] = true;
                backtrack1(index + 1);
                temp.deleteCharAt(index);
                visited[i] = false;
            }
        }
    }


    /**
     * leetcode 77.组合
     * 给定两个整数 n 和 k，返回范围 [1, n] 中所有可能的 k 个数的组合。
     *
     * 你可以按 任何顺序 返回答案。
     * @throws Exception
     */
    private void test2() throws Exception{
        int n = 5;
        int k = 3;
        List<List<Integer>> res = combine(n, k);
        System.out.println(res);
    }

    public List<List<Integer>> combine(int n, int k) {
        List<List<Integer>> res = new ArrayList<>();
        if (k <= 0 || n < k) {
            return res;
        }
        Deque<Integer> path = new ArrayDeque<>();
        dfs(n, k, 1, path, res);
        return res;
    }

    private void dfs(int n, int k, int begin, Deque<Integer> path, List<List<Integer>> res) {
        // 递归终止条件是：path 的长度等于 k
        if (path.size() == k) {
            res.add(new ArrayList<>(path));
            return;
        }
        for (int i = begin; i <= n; i++) {
            path.addLast(i);
            System.out.println("递归之前 => " + path);
            // 下一轮搜索，设置的搜索起点要加 1，因为组合数理不允许出现重复的元素
            dfs(n, k, i + 1, path, res);
            path.removeLast();
            System.out.println("递归之后 => " + path);
        }
    }



}
