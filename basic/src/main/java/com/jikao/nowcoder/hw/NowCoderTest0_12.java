package com.jikao.nowcoder.hw;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
 * TODO
 * 57
 */
public class NowCoderTest0_12 {


    public static void main(String[] args) throws Exception {
//        test56();
//        test57();
//        test58();
        test59();
//        test60();
    }

    /**
     * HJ56 完全数计算
     */
    public static void test56() {
        Scanner in = new Scanner(System.in);
        while (in.hasNext()) {
            int inputInt = in.nextInt();
            System.out.println(count(inputInt));
        }
    }

    public static int conut2(int n){
        if(n<6){
            return 0;
        } else if(n<28){
            return 1;
        }else if(n<496){
            return 2;
        } else if(n<8128){
            return 3;
        } else if(n<33550336){
            return 4;
        } else {
            return -1;
        }
    }

    public static int count(int n){
        int result = 0;
        for(int i =1;i<n;i++){
            int sum = 0;
            for(int j=1;j<=i/2;j++){
                if(i%j==0){
                    sum += j;
                }
            }
            if(sum == i){
                result ++;
            }
        }
        return result;
    }

    /**
     * HJ57 高精度整数加法
     * @throws Exception
     */
    public static void test57() throws Exception{
        BufferedReader buff = new BufferedReader(new InputStreamReader(System.in));
        String str;
        while ((str = buff.readLine()) != null) {
            // 1. 转成sb，方便操作
            StringBuilder sb1 = new StringBuilder(str.trim());
            StringBuilder sb2 = new StringBuilder(buff.readLine().trim());

            // 2. 对齐，通过补前缀0的方式
            // s1:  006450
            // s2:  630222
            while (sb1.length() != sb2.length()) {
                if (sb1.length() < sb2.length()) {
                    sb1.insert(0, '0');
                } else {
                    sb2.insert(0, '0');
                }
            }

            // 3.计算
            int carry = 0;
            StringBuilder resultSB = new StringBuilder();
            for (int i = sb1.length() - 1; i >= 0; i--) {
                int a = sb1.charAt(i) - '0';
                int b = sb2.charAt(i) - '0';

                int tmpSum = a + b + carry;
                if (tmpSum > 9) {
                    tmpSum = tmpSum % 10;
                    carry = 1;
                } else {
                    carry = 0;
                }
                resultSB.insert(0, tmpSum);
            }
            // 4. 判断有无进位
            if (carry == 1) {
                resultSB.insert(0, '1');
            }

            // 5. 输出
            System.out.println(resultSB);
        }
    }


    /**
     * HJ58 输入n个整数，输出其中最小的k个
     * @throws Exception
     */
    public static void test58() throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String str = null;
        while ((str = br.readLine()) != null) {
            if (str.equals("")) continue;
            String[] params = str.split(" ");
            int n = Integer.parseInt(params[0]), k = Integer.parseInt(params[1]);
            int[] res = new int[n];
            int start = 0, index = 0;
            if (params.length > 2) start = 2;
            else params = br.readLine().split(" ");
            for (int i = start; i < params.length; i++) {
                res[index++] = Integer.parseInt(params[i]);
            }
            Arrays.sort(res);
            StringBuilder ans = new StringBuilder();
            for (int i = 0; i < k; i++) ans.append(res[i]).append(" ");
            System.out.println(ans.toString().trim());
        }
    }

    /**
     * HJ59 找出字符串中第一个只出现一次的字符
     * @throws Exception
     */
    public static void test59() throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String input;
        while ((input = br.readLine()) != null) {
            for (int i = 0; i < input.length(); i++) {
                char c = input.charAt(i);
                if (input.indexOf(c) == input.lastIndexOf(c)) {
                    System.out.println(c);
                    break;
                }
                if (i == input.length() - 1) {
                    System.out.println(-1);
                }
            }
        }
    }

    private static void my59() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String str;
        while((str = br.readLine())!=null){
            Map<Character, Integer> map = new LinkedHashMap<>();
            char[] charArray = str.toCharArray();
            for (int i = 0; i < charArray.length; i++) {
                char c = charArray[i];
                if (map.containsKey(c)) {
                    map.put(c, map.get(c) + 1);
                } else {
                    map.put(c, 1);
                }
            }
            int min = 0;
            Set<Map.Entry<Character, Integer>> entrySet = map.entrySet();
            for (Map.Entry<Character, Integer> entry : entrySet) {
                Integer value = entry.getValue();
                if (min == 0) {
                    min = value;
                } else {
                    min = Math.min(min, value);
                }
            }
            if (min > 1) {
                System.out.println(-1);
                continue;
            }
            for (Map.Entry<Character, Integer> entry : entrySet) {
                Integer value = entry.getValue();
                if (min == value) {
                    System.out.println(entry.getKey());
                    break;
                }
            }
        }
    }

    /**
     * (3) *HJ60.查找组成一个偶数最接近的两个素数
     * 描述
     * 任意一个偶数（大于2）都可以由2个素数组成，组成偶数的2个素数有很多种情况，本题目要求输出组成指定偶数的两个素数差值最小的素数对。
     *
     * 数据范围：输入的数据满足 4≤n≤1000
     * 输入描述：
     * 输入一个大于2的偶数
     *
     * 输出描述：
     * 从小到大输出两个素数
     *
     * 输入：
     * 20
     * 输出：
     * 7
     * 13
     *
     * 输入：
     * 4
     * 输出：
     * 2
     * 2
     * @throws Exception
     */
    public static void test60() throws Exception {
        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
        String str = "";
        while((str = bf.readLine()) != null){
            int num = Integer.parseInt(str.trim());
            for(int m = num/2;num >= 2; m--){
                if(isZhiShu(m) && isZhiShu(num - m)){
                    System.out.println(m);
                    System.out.println(num - m);
                    break;
                }
            }
        }

    }

    private static void my60() throws IOException {
        List<Integer> zhisuList = new ArrayList<>(512);
        // 1、先找出1000内的所有质数
        for (int i = 2; i <= 1000; i++) {
            if (isZhiShu(i)) {
                zhisuList.add(i);
            }
        }
        // 2、组成指定偶数的两个素数差值最小的素数对
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String line = null;
        while((line = br.readLine()) != null) {
            int max = 0;
            int num1 = 0;
            int num2 = 0;
            int target = Integer.parseInt(line);
            for (int i = 0; i < zhisuList.size(); i++) {
                int a = zhisuList.get(i);
                if (a > target) {
                    break;
                }
                for (int j = i; j < zhisuList.size(); j++) {
                    int b = zhisuList.get(j);
                    if (b > target) {
                        break;
                    }
                    int sum = a + b;
                    if (sum == target) {
                        num1 = a;
                        num2 = b;
                        max = Math.max(max, b-a);
                    }
                }
            }
            System.out.println(num1);
            System.out.println(num2);
        }
    }

    private static boolean isZhiShu(int num){
        for(int n = 2; n < num;n++){
            if(num % n == 0){
                return false;
            }
        }
        return true;
    }


}
