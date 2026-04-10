package com.jikao.nowcoder.hw;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Scanner;

public class NowCoderTest0_1 {


    public static void main(String[] args) throws Exception {
//        test1();
//        test2();
//        test3();
//        test4();
        test5();
    }

    /**
     * hello nowcoder
     *
     * 8
     */
    public static void test1() {
        Scanner in = new Scanner(System.in);
        while (in.hasNext()) {
            String str = in.nextLine();
            String[] tempArray = str.split(" ");
            int length = tempArray[tempArray.length - 1].length();
            System.out.println(length);
        }
    }

    /**
     * 输入：
     * ABCabc
     * A
     * 输出：
     * 2
     */
    public static void test2() {
        Scanner in = new Scanner(System.in);
        String s1 = in.nextLine().toLowerCase();
        String s2 = in.nextLine().toLowerCase();
        int lenBefore = s1.length();
        int lenAfter = s1.replaceAll(s2, "").length();
        System.out.print(lenBefore - lenAfter);
    }

    /**
     * 输入：
     * 3
     * 2
     * 2
     * 1
     *
     * 输出：
     * 1
     * 2
     */
    public static void test3() throws Exception {
        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
        String str;
        while ((str = bf.readLine()) != null) {
            boolean[] flagArrays = new boolean[1001];
            StringBuilder sb = new StringBuilder();
            int n = Integer.parseInt(str);
            for (int i = 0; i < n; i++){
                flagArrays[Integer.parseInt(bf.readLine())] = true;
            }

            for (int i = 0; i < 1001; i++){
                if (flagArrays[i]) {
                    sb.append(i).append("\n");
                }
            }

            sb.deleteCharAt(sb.length() - 1);
            System.out.println(sb);
        }
    }

    /**
     * 输入：
     * abc
     *
     * 输出：
     * abc00000
     * @throws Exception
     */
    public static void test4() throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String str;
        while((str = br.readLine())!=null){
            int len = str.length();
            int start = 0;
            while (len >= 8){
                System.out.println(str.substring(start, start + 8));
                start += 8;
                len -= 8;
            }
            if (len > 0) {
                char[] tmp = new char[8];
                for(int i = 0;i<8;i++){
                    tmp[i]='0';
                }
                for(int i = 0; start < str.length(); i++) {
                    tmp[i] = str.charAt(start++);
                }
                System.out.println(String.valueOf(tmp));
            }
        }
    }

    /**
     * 写出一个程序，接受一个十六进制的数，输出该数值的十进制表示。
     *
     * 输入：
     * 0xAA
     *
     * 输出：
     * 170
     * @throws Exception
     */
    public static void test5() throws Exception {
        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
        String input;
        while((input = bf.readLine())!=null){
            String temp = input.substring(0, 2);
            int sum = 0;
            int length = temp.length();
            for(int i= length-1;i>=0;i--){
                char c = temp.charAt(i);
                int tempNum = c;
                if(tempNum>=65){
                    tempNum = tempNum - 65 + 10;
                }else{
                    tempNum = tempNum - 48;
                }
                sum = sum + (int) Math.pow(16, length-i-1)*tempNum;
            }
            System.out.println(sum);
        }
    }

}
