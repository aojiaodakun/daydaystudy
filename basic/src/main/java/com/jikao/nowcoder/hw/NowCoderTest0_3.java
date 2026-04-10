package com.jikao.nowcoder.hw;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Scanner;

public class NowCoderTest0_3 {

    public static void main(String[] args) throws Exception {
//        test11();
//        test12();
//        test13();
//        test14();
        test15();
    }

    /**
     * HJ11 数字颠倒
     * 输入描述：
     * 输入一个int整数
     *
     * 输出描述：
     * 将这个整数以字符串的形式逆序输出
     *
     * 输入：
     * 1516000
     *
     * 输出：
     * 0006151
     */
    public static void test11() {
        Scanner in = new Scanner(System.in);
        while (in.hasNext()) {
            String str = in.nextLine();
            char[] chars = str.toCharArray();
            for (int i = chars.length-1; i >= 0; i--) {
                System.out.print(chars[i]);
            }
        }
    }

    /**
     * HJ12 字符串反转
     * 描述
     * 接受一个只包含小写字母的字符串，然后输出该字符串反转后的字符串。（字符串长度不超过1000）
     *
     * 输入描述：
     * 输入一行，为一个只包含小写字母的字符串。
     *
     * 输出描述：
     * 输出该字符串反转后的字符串。
     *
     * 输入：
     * abcd
     *
     * 输出：
     * dcba
     */
    public static void test12() {
        Scanner in = new Scanner(System.in);
        while (in.hasNext()) {
            String str = in.nextLine();
            char[] chars = str.toCharArray();
            for (int i = chars.length-1; i >= 0; i--) {
                System.out.print(chars[i]);
            }
        }
    }

    /**
     * HJ13 句子逆序
     * 输入描述：
     * 输入一个英文语句，每个单词用空格隔开。保证输入只包含空格和字母。
     *
     * 输出描述：
     * 得到逆序的句子
     *
     * 输入：
     * I am a boy
     *
     * 输出：
     * boy a am I
     * @throws Exception
     */
    public static void test13() throws Exception{
        Scanner in = new Scanner(System.in);
        while (in.hasNext()) {
            String str = in.nextLine();
            String[] strArray = str.split(" ");
            for (int i = strArray.length-1; i >=0 ; i--) {
                System.out.print(strArray[i] + " ");
            }
        }
    }

    /**
     * (2) *HJ14.字符串排序
     * 描述
     * 给定 n 个字符串，请对 n 个字符串按照字典序排列。
     *
     * 数据范围： 1≤n≤1000  ，字符串长度满足1≤len≤100
     * 输入描述：
     * 输入第一行为一个正整数n(1≤n≤1000),下面n行为n个字符串(字符串长度≤100),字符串中只含有大小写字母。
     * 输出描述：
     * 数据输出n行，输出结果为按照字典序排列的字符串。
     *
     * 输入：
     * 9
     * cap
     * to
     * cat
     * card
     * two
     * too
     * up
     * boat
     * boot
     *
     * 输出：
     * boat
     * boot
     * cap
     * card
     * cat
     * to
     * too
     * two
     * up
     * @throws Exception
     */
    public static void test14() throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int count = Integer.parseInt(br.readLine());
        String[] data = new String[count];
        for(int i=0;i<data.length;i++){
            data[i] = br.readLine();
        }
        StringBuilder sb = new StringBuilder();
        Arrays.sort(data);
        for(String s:data){
            sb.append(s+"\n");
        }
        System.out.print(sb.toString());
    }

    /**
     * HJ15 求int型正整数在内存中存储时1的个数
     * 描述
     * 输入一个 int 型的正整数，计算出该 int 型数据在内存中存储时 1 的个数。
     *
     * 数据范围：保证在 32 位整型数字范围内
     * 输入描述：
     * 输入一个整数（int类型）
     *
     * 输出描述：
     * 这个数转换成2进制后，输出1的个数
     *
     * 输入：
     * 5
     * 输出：
     * 2
     *
     * 输入：
     * 0
     * 输出：
     * 0
     *
     * @throws Exception
     */
    public static void test15() throws Exception {
        InputStream in = System.in;
        int num;
        byte[] bytes = new byte[1024];
        while((num = in.read(bytes)) > 0){
            String str = new String(bytes,0,num-1);
            int num2 = Integer.parseInt(str);

            int count = 0;
            while(num2 != 0){
                count++;
                num2 = num2 & (num2 - 1);
            }
            System.out.println(count);
        }
    }

}
