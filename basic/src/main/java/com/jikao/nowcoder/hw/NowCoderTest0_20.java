package com.jikao.nowcoder.hw;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.Scanner;


public class NowCoderTest0_20 {


    public static void main(String[] args) throws Exception {
//        test96();
//        test97();
//        test98();
//        test99();
        test100();
    }

    /**
     * HJ96 表示数字
     * @throws Exception
     */
    public static void test96() throws Exception{
        Scanner in = new Scanner(System.in);
        while (in.hasNext()) { // 注意 while 处理多个 case
            StringBuilder sb = new StringBuilder();
            char[] charArray = in.nextLine().toCharArray();

            boolean flag = false;
            for (int i = 0; i < charArray.length; i++) {
                char c = charArray[i];
                if (c >= '0' && c <='9') {
                    if (!flag) {
                        sb.append("*");
                        flag = true;
                    }
                    sb.append(c);
                } else {
                    if (flag) {
                        sb.append("*");
                    }
                    sb.append(c);
                    flag = false;
                }
            }
            if (flag) {
                sb.append("*");
            }
            System.out.println(sb);

        }
    }

    /**
     * HJ97 记负均正
     * @throws Exception
     */
    public static void test97() throws Exception{
        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
        String str;
        while ((str = bf.readLine()) != null) {
            int size = Integer.parseInt(str);
            String line = bf.readLine();
            double sum = 0;
            int zhengshuCount = 0;
            int fushuCount = 0;
            String[] strArray = line.split(" ");
            for (int i = 0; i < strArray.length; i++) {
                int intC = Integer.parseInt(strArray[i]);
                if (intC > 0) {
                    sum = sum+intC;
                    zhengshuCount++;
                } else if (intC < 0) {
                    fushuCount++;
                }
            }
            double avg = 0.0;
            if (zhengshuCount > 0) {
                avg = sum/zhengshuCount;
                DecimalFormat df = new DecimalFormat("#0.0");
                System.out.println(fushuCount + " " + df.format(avg));
            } else {
                System.out.println(fushuCount + " " + avg);
            }

        }
    }

    /**
     * HJ98 自动售货系统，TODO
     * @throws Exception
     */
    public static void test98() throws Exception{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String str;
        while ((str = br.readLine()) != null) {

        }
    }


    /**
     * HJ99 自守数
     */
    public static void test99() throws Exception{
        Scanner in = new Scanner(System.in);
        while (in.hasNextInt()) {
            int n = in.nextInt();
            int count = 1;
            for (int i = 1; i <= n; i++) {
                int pow = i * (i-1);
                int length = String.valueOf(i).length();
                if (pow % (Math.pow(10, length)) == 0) {
                    count++;
                }
            }
            System.out.println(count);
        }
    }


    /**
     * HJ100 等差数列
     * @throws Exception
     */
    public static void test100() throws Exception{
        Scanner in = new Scanner(System.in);
        while (in.hasNextInt()) {
            int n = in.nextInt();
            int first = 2;
            int last = 2 + 3*(n-1);
            int mid = first+last;
            if (n%2==1) {
                System.out.println(n/2*mid+mid/2);
            } else {
                System.out.println(n/2*mid);
            }
        }
    }

}
