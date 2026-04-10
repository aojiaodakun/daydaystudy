package com.jikao.nowcoder.hw;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StreamTokenizer;
import java.util.Scanner;

public class NowCoderTest0_2 {


    public static void main(String[] args) throws Exception {
//        test6();
//        test7();
//        test8();
        test9();
//        test10();
    }

    /**
     * HJ6 质数因子
     * 功能:输入一个正整数，按照从小到大的顺序输出它的所有质因子（重复的也要列举）（如180的质因子为2 2 3 3 5 ）
     *
     * 输入：
     * 180
     *
     * 输出：
     * 2 2 3 3 5
     */
    public static void test6() {
        Scanner scanner = new Scanner(System.in);
        long num = scanner.nextLong();
        // 开方
        long k = (long) Math.sqrt(num);

        for (long i = 2; i <= k; ++i) {
            while (num % i == 0) {
                System.out.print(i + " ");
                num /= i;
            }
        }
        System.out.println(num == 1 ? "": num+" ");
    }


    /**
     * HJ7 取近似值
     * 描述
     * 写出一个程序，接受一个正浮点数值，输出该数值的近似整数值。如果小数点后数值大于等于 0.5 ,向上取整；小于 0.5 ，则向下取整。
     *
     * 数据范围：保证输入的数字在 32 位浮点数范围内
     *
     * 输入：
     * 5.5
     * 复制
     * 输出：
     * 6
     * 说明：
     * 0.5>=0.5，所以5.5需要向上取整为6
     *
     * 输入：
     * 2.499
     * 输出：
     * 2
     * 说明：
     * 0.499<0.5，2.499向下取整为2
     */
    public static void test7() {
        Scanner in = new Scanner(System.in);
        while (in.hasNext()) {
            double d1 = in.nextDouble();
            double d2 = d1-(int)d1;
            if (d2 >= 0.5) {
                System.out.println((int)Math.ceil(d1));
            } else {
                System.out.println((int)Math.floor(d1));
            }
        }
    }

    /**
     * (1) HJ8.合并表记录
     *数据表记录包含表索引index和数值value（int范围的正整数），请对表索引相同的记录进行合并，即将相同索引的数值进行求和运算，输出按照index值升序进行输出。
     *
     * 提示:
     * 0 <= index <= 11111111
     * 1 <= value <= 100000
     *
     * 输入描述：
     * 先输入键值对的个数n（1 <= n <= 500）
     * 接下来n行每行输入成对的index和value值，以空格隔开
     *
     * 输出描述：
     * 输出合并后的键值对（多行）
     *
     * 输入：
     * 4
     * 0 1
     * 0 2
     * 1 2
     * 3 4
     *
     * 输出：
     * 0 3
     * 1 2
     * 3 4
     */
    public static void test8() throws Exception{
        StreamTokenizer st = new StreamTokenizer(new BufferedReader(new InputStreamReader(System.in)));
        st.nextToken();      // 分隔符
        int n = (int) st.nval;   // nextValue
        int[] arr = new int[n];

        for (int i = 0; i < n; i++) {
            st.nextToken();
            int key = (int) st.nval;
            st.nextToken();
            int value = (int) st.nval;
            arr[key] = arr[key] + value;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arr.length ; i++) {
            if(arr[i] != 0){
                sb.append(i).append(" ").append(arr[i]).append("\n");
            }
        }
        System.out.println(sb);
    }

    /**
     * HJ9 提取不重复的整数
     * 输入一个 int型整数，按照从右向左的阅读顺序，返回一个不含重复数字的新的整数。
     * 保证输入的整数最后一位不是 0 。
     *
     * 输入：
     * 9876673
     *
     * 输出：
     * 37689
     * @throws Exception
     */
    public static void test9() throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String str;
        while((str = br.readLine())!=null){
            char[] chars = str.toCharArray();
            boolean[] flags = new boolean[10];
            for (int i = chars.length - 1; i >= 0; i--) {
                int tempInt = chars[i] - 48;
                if (!flags[tempInt]) {
                    flags[tempInt] = true;
                    System.out.print(tempInt);
                }
            }
        }
    }

    /**
     * HJ10 字符个数统计
     *
     * 输入：
     * abc
     * 输出：
     * 3
     *
     * 输入：
     * aaa
     * 输出：
     * 1
     * @throws Exception
     */
    public static void test10() throws Exception {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String line = bufferedReader.readLine();
        int[] a = new int[128];
        int count=0;
        for(int i=0;i<line.length();i++){
            char b = line.charAt(i);
            if(a[b]==0){
                count++;
                a[b]=1;
            }
        }
        System.out.println(count);
    }

}
