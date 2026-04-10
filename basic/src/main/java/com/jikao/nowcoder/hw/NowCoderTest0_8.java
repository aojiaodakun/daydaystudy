package com.jikao.nowcoder.hw;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Scanner;

/**
 * 未理解
 * 39
 */
public class NowCoderTest0_8 {

    public static void main(String[] args) throws Exception {
//        test36();
//        test37();
//        test38();
//        test39();
        test40();
    }

    /**
     * HJ36 字符串加密
     * @throws Exception
     */
    public static void test36() throws Exception{
        Scanner in = new Scanner(System.in);
        // 注意 hasNext 和 hasNextLine 的区别
        while (in.hasNext()) { // 注意 while 处理多个 case
            char[] originZimuChars = new char[26];
            char[] charArray = in.nextLine().toCharArray();
            char[] newCharArray = new char[26];
            int index = 0;
            for (int i = 0; i < charArray.length; i++) {
                if (originZimuChars[charArray[i] - 97] == 0) {
                    originZimuChars[charArray[i] - 97] = charArray[i];
                    newCharArray[index] = charArray[i];
                    index++;
                }
            }
            for (int i = 0; i < originZimuChars.length; i++) {
                if (originZimuChars[i] == 0) {
                    newCharArray[index] = (char)(i+97);
                    index++;
                }
            }
            String text = in.nextLine();
            char[] textCharArray = text.toCharArray();
            char[] newTextCharArray = new char[text.length()];
            for (int i = 0; i < textCharArray.length; i++) {
                char c = textCharArray[i];
                int cIndex = c - 97;
                newTextCharArray[i] = newCharArray[cIndex];
            }
            System.out.println(new String(newTextCharArray));
        }
    }

    /**
     * HJ37 统计每个月兔子的总数
     * @throws Exception
     */
    public static void test37() throws Exception{
        Scanner in = new Scanner(System.in);
        // 注意 hasNext 和 hasNextLine 的区别
        while (in.hasNext()) { // 注意 while 处理多个 case
            int month = in.nextInt();
            if (month < 3) {
                System.out.println(1);
                continue;
            }
            int newTuzi1 = 1;
            int newTuzi2 = 0;
            int oldTuzi = 1;
            for (int i = 4; i <= month; i++) {
                oldTuzi = oldTuzi + newTuzi2;
                newTuzi2 = newTuzi1;
                newTuzi1 = oldTuzi;
            }
            System.out.println(newTuzi1 + newTuzi2 + oldTuzi);
        }
    }


    /**
     * HJ38 求小球落地5次后所经历的路程和第5次反弹的高度
     * @throws Exception
     */
    public static void test38() throws Exception{
        Scanner in = new Scanner(System.in);
        // 注意 hasNext 和 hasNextLine 的区别
        while (in.hasNext()) { // 注意 while 处理多个 case
            double height = in.nextDouble();
            System.out.println(get(height));
            System.out.println(height(height));
        }
    }
    public static double height(double h) {
        for (int i = 0; i < 5; i++) {
            h = h / 2;
        }
        return h;
    }

    public static double get(double h) {
        return h + h + h / 2 + h / 4 + h / 8;
    }

    /**
     * HJ39 判断两个IP是否属于同一子网
     * @throws Exception
     */
    public static void test39() throws Exception {

    }

    /**
     * HJ34 图片整理
     * @throws Exception
     */
    public static void test34() throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String str;
        while((str = br.readLine())!=null){
            char[] charArray = str.toCharArray();
            Arrays.sort(charArray);
            System.out.println(new String(charArray));
        }
    }

    /**
     * HJ40 统计字符
     * @throws Exception
     */
    public static void test40() throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String s;
        while((s = br.readLine()) != null){
            char[] chs = s.toCharArray();
            int letter = 0,space = 0,num = 0,other = 0;
            for(int i = 0; i < chs.length; i ++){
                if(chs[i] >= 65 && chs[i] <= 90 || chs[i] >= 97 && chs[i] <= 122){
                    letter++;
                }else if(chs[i] == 32){
                    space++;
                }else if(chs[i] >= 48 && chs[i] <= 57){
                    num++;
                }else{
                    other++;
                }
            }
            System.out.println(letter + "\n" + space + "\n" + num + "\n" + other);
        }
    }

}
