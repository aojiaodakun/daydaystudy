package com.jikao.enterprise.hw.exam;

import com.hzk.datastructure.dfs.DfsTest;
import com.jikao.nowcoder.hw.NowCoderTest0_1;
import com.jikao.nowcoder.hw.NowCoderTest0_11;

import java.util.Scanner;

/**
 * 未理解
 * 4.1、NC60.排队序列
 * 回溯
 * 5.1、*leetcode 面试题08.08.有重复字符串的排列组合
 * 5.2、leetcode 77.组合
 *
 * 6.3、NC28.最小覆盖子串
 *
 * 9.2、*HJ28.素数伴侣
 */

/**
 * 动态规划问题
 * dfsProblem
 */
public class ExamTest1 {

    public static void main(String[] args) throws Exception {
//        test1();
//        test2();
        test3();
    }

    private static void dfsProblem() throws Exception{
        // 01背包问题
        DfsTest.packageProblem();
        // HJ52 计算字符串的编辑距离
        NowCoderTest0_11.test52();
        //

    }

    /**
     * 1.汽水瓶
     */
    public static void test1(){
        Scanner in = new Scanner(System.in);
        // 注意 hasNext 和 hasNextLine 的区别
        while (in.hasNextInt()) {
            int count = 0;
            int blank = in.nextInt();

            while (blank > 2) {
                int water = blank / 3;
                count += water;
                blank = blank % 3 + water;
                if (blank == 2) {
                    count++;
                    break;
                }
            }
            if (count > 0) {
                System.out.println(count);
            }
        }
    }

    /**
     * 2.明明的随机数
     */
    public static void test2() throws Exception{
        Scanner in = new Scanner(System.in);
        while (in.hasNextInt()) {
            int size = in.nextInt();
            int count = 0;
            boolean[] flagArray = new boolean[1000];
            while (in.hasNextInt()) {
                count++;
                flagArray[in.nextInt()] = true;
                if (count == size) {
                    break;
                }
            }
            // 遍历flagArray获取数值
            for (int i = 0; i < flagArray.length; i++) {
                if (flagArray[i]) {
                    System.out.println(i);
                }
            }
        }
    }

    /**
     * 3.进制转换
     * 写出一个程序，接受一个十六进制的数，输出该数值的十进制表示。
     *
     * 输入例子：
     * 0xAA
     * 输出例子：
     * 170
     * @throws Exception
     */
    public static void test3() throws Exception{
        NowCoderTest0_1.test5();
    }

}
