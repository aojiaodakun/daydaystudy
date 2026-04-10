package com.jikao.enterprise.hw.doublepoint;

import java.util.*;

/**
 * 6.双指针（3题）
 * (1) *leetcode 674.最长连续递增序列
 * (2) NC5.最长回文子串
 * (3) NC76.最小覆盖子串
 */
public class HwDoublePointTest1 {


    public static void main(String[] args) throws Exception {
        HwDoublePointTest1 my = new HwDoublePointTest1();
//        test1();
        my.test2();
//        my.test3();
//        test4();
    }

    /**
     * (1) *leetcode 674.最长连续递增序列
     * 输入：nums = [1,3,5,4,7]
     * 输出：3
     *
     * 输入：nums = [2,2,2,2,2]
     * 输出：1
     * @throws Exception
     */
    private static void test1() throws Exception{
        int[] nums = new int[]{1,3,5,4,7};
        int length = findLengthOfLCIS(nums);
        System.out.println(length);
    }

    private static int findLengthOfLCIS(int[] nums) {
        int maxLength = 0;
        int start = 0;
        for (int i = 0; i < nums.length; i++) {
            if (i > 0 && nums[i] <= nums[i - 1]) {
                start = i;
            }
            maxLength = Math.max(maxLength, i - start + 1);
        }
        return maxLength;
    }

    /**
     * (2) NC5.最长回文子串
     * 输入：s = "babad"
     * 输出："bab"
     * 解释："aba" 同样是符合题意的答案。
     *
     * 输入：s = "cbbd"
     * 输出："bb"
     * @throws Exception
     */
    private void test2() throws Exception{
        String str1 = "babad";
        String result1 = longestPalindrome(str1);
        System.out.println(result1);

        System.out.println("----------");
        String str2 = "cbbd";
        String result2 = longestPalindrome(str2);
        System.out.println(result2);
    }

    /**
     * 方法二：中心扩展算法
     * @param s
     * @return
     */
    public String longestPalindrome(String s) {
        if (s == null || s.length() < 1) {
            return "";
        }
        int maxLen = 1;
        int begin = 0;

        char[] charArray = s.toCharArray();
        for (int i = 0; i < s.length()-1; i++) {
            int oddLen = expandAroundCenter(charArray, i, i);
            int evenLen = expandAroundCenter(charArray, i, i+1);

            int curMaxLen = Math.max(oddLen, evenLen);
            if (curMaxLen > maxLen) {
                maxLen = curMaxLen;
                begin = i - (maxLen - 1)/2;
            }
        }
        return s.substring(begin, begin + maxLen);
    }

    /**
     *
     * @param charArray
     * @param left
     * @param right
     * @return 回文串长度
     */
    public int expandAroundCenter(char[] charArray, int left, int right) {
        // left = right，回文中心是一个字符，回文串长度为奇数
        // left = right+1，回文中心是两个字符，回文串长度为偶数
        int len = charArray.length;
        int i = left;
        int j = right;
        while (i >=0 && j < len) {
            if (charArray[i] == charArray[j]) {
                i--;
                j++;
            } else {
                break;
            }
        }

        // j-i+1-2=j-i-1
        return j-i-1;
    }


    Map<Character, Integer> ori = new HashMap<>();
    Map<Character, Integer> cnt = new HashMap<>();

    /**
     * (3) NC76.最小覆盖子串
     * 给你一个字符串 s 、一个字符串 t 。返回 s 中涵盖 t 所有字符的最小子串。如果 s 中不存在涵盖 t 所有字符的子串，则返回空字符串 "" 。
     *
     * 输入：s = "ADOBECODEBANC", t = "ABC"
     * 输出："BANC"
     * 解释：最小覆盖子串 "BANC" 包含来自字符串 t 的 'A'、'B' 和 'C'。
     *
     * @throws Exception
     */
    private void test3() throws Exception{
        String s = "ADOBECODEBANC";
        String t = "ABC";
        String s1 = minWindow(s, t);
        System.out.println(s1);
    }

    public String minWindow(String s, String t) {
        int tLen = t.length();
        for (int i = 0; i < tLen; i++) {
            char c = t.charAt(i);
            ori.put(c, ori.getOrDefault(c, 0) + 1);
        }
        int l = 0, r = -1;
        int len = Integer.MAX_VALUE, ansL = -1, ansR = -1;
        int sLen = s.length();
        while (r < sLen) {
            ++r;
            if (r < sLen && ori.containsKey(s.charAt(r))) {
                cnt.put(s.charAt(r), cnt.getOrDefault(s.charAt(r), 0) + 1);
            }
            while (check() && l <= r) {
                if (r - l + 1 < len) {
                    len = r - l + 1;
                    ansL = l;
                    ansR = l + len;
                }
                if (ori.containsKey(s.charAt(l))) {
                    cnt.put(s.charAt(l), cnt.getOrDefault(s.charAt(l), 0) - 1);
                }
                ++l;
            }
        }
        return ansL == -1 ? "" : s.substring(ansL, ansR);
    }

    public boolean check() {
        for (Map.Entry<Character, Integer> entry : ori.entrySet()) {
            Character key = entry.getKey();
            Integer val = entry.getValue();
            if (cnt.getOrDefault(key, 0) < val) {
                return false;
            }
        }
        return true;
    }




}
