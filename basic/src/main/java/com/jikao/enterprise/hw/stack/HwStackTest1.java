package com.jikao.enterprise.hw.stack;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * 4.栈（2题）
 * (1) NC60.排队序列
 * (2) *leetcode 1614.括号的最大嵌套深度
 */
public class HwStackTest1 {

    public static void main(String[] args) throws Exception {
        test1();

//        test2();
    }

    /**
     * (1) NC60.排队序列
     * 知识点：康托逆展开，https://blog.csdn.net/qq_40061421/article/details/81915838
     * https://leetcode.cn/problems/permutation-sequence/solution/di-kge-pai-lie-by-leetcode-solution/
     * 给出集合 [1,2,3,...,n]，其所有元素共有 n! 种排列。
     * 按大小顺序列出所有排列情况，并一一标记，当 n = 3 时, 所有排列如下：
     * "123"
     * "132"
     * "213"
     * "231"
     * "312"
     * "321"
     * 给定 n 和 k，返回第 k 个排列。
     *
     * 示例 1：
     *
     * 输入：n = 3, k = 3
     * 输出："213"
     *
     * 示例 2：
     *
     * 输入：n = 4, k = 9
     * 输出："2314"
     *
     * 提示：
     * 1 <= n <= 9
     * 1 <= k <= n!
     */
    private static void test1() throws Exception{
//        String permutation = getPermutation(3, 3);
//        System.out.println(permutation);
        String permutation1 = getPermutation(4, 9);
        System.out.println(permutation1);
    }

    private static String getPermutation(int n, int k) {
        StringBuilder sb = new StringBuilder();
        // 候选数字
        List<Integer> candidates = new ArrayList<>();
        // 分母的阶乘数
        int[] factorials = new int[n+1];
        factorials[0] = 1;
        int fact = 1;
        for(int i = 1; i <= n; ++i) {
            candidates.add(i);
            fact *= i;
            factorials[i] = fact;
        }
        k--;
        for(int i = n-1; i >= 0; --i) {
            // 计算候选数字的index
            int index = k / factorials[i];
            sb.append(candidates.remove(index));
            k -= index*factorials[i];
        }
        return sb.toString();
    }


    /**
     * (2) *leetcode 1614.括号的最大嵌套深度
     *
     * 输入：s = "(1)+((2))+(((3)))"
     * 输出：3
     */
    private static void test2() throws Exception{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String str;
        while ((str = br.readLine()) != null) {
            String[] strArray = str.split("=");
            String inputString = strArray[1].trim();
            int maxDepth = maxDepth(inputString);
            System.out.println(maxDepth);
        }
    }

    public static int maxDepth(String str) {
        Stack<Character> stack = new Stack<>();
        int maxDepth = 0;
        char[] charArray = str.toCharArray();
        for (int i = 0; i < charArray.length; i++) {
            char tempChar = charArray[i];
            if (tempChar == '(') {
                stack.push(tempChar);
            } else if(tempChar == ')'){
                maxDepth = Math.max(maxDepth, stack.size());
                stack.pop();
            }
        }
        return maxDepth;
    }

}
