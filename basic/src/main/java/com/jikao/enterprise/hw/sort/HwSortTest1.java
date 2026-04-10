package com.jikao.enterprise.hw.sort;

import com.jikao.leetcode.LeetCodeTest1;
import com.jikao.nowcoder.hw.*;

/**
 * 3.排序（5题）
 * (1) HJ8.合并表记录
 * (2) *HJ14.字符串排序
 * (3) HJ27.查找兄弟单词
 * (4) *NC37.合并区间
 * (5) *HJ68.成绩排序
 */
public class HwSortTest1 {

    public static void main(String[] args) throws Exception {
//        test1();
//        test2();
        test3();
//        test4();
//        test5();
    }

    /**
     * (1) HJ8.合并表记录
     */
    private static void test1() throws Exception{
        NowCoderTest0_2.test8();
    }

    /**
     * (2) *HJ14.字符串排序
     */
    private static void test2() throws Exception{
        NowCoderTest0_3.test14();
    }

    /**
     * (3) HJ27.查找兄弟单词
     */
    private static void test3() throws Exception{
        NowCoderTest0_6.test27();
    }

    /**
     * (4) *NC37.合并区间
     */
    private static void test4() throws Exception{
        LeetCodeTest1.mergeMethod();
    }

    /**
     * (5) *HJ68.成绩排序
     */
    private static void test5() throws Exception{
        NowCoderTest0_14.test68();
    }


}
