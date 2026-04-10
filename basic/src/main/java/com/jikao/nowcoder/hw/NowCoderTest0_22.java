package com.jikao.nowcoder.hw;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Scanner;


public class NowCoderTest0_22 {


    public static void main(String[] args) throws Exception {
//        test106();
//        test107();
        test108();
    }

    /**
     * HJ106 字符逆序
     * @throws Exception
     */
    public static void test106() throws Exception{
        Scanner in = new Scanner(System.in);
        while (in.hasNext()) { // 注意 while 处理多个 case
            String str = in.nextLine();
            char[] charArray = str.toCharArray();
            for (int i = charArray.length-1; i >=0; i--) {
                System.out.print(charArray[i]);
            }
        }
    }

    /**
     * HJ107 求解立方根
     *
     * 计算一个浮点数的立方根，不使用库函数。
     * 保留一位小数。
     * @throws Exception
     */
    public static void test107() throws Exception{
        Scanner sc = new Scanner(System.in);
        while (sc.hasNextDouble()) {
            double num = sc.nextDouble();
            double x = dichotomy(num);
            System.out.printf("%.1f", x);
        }
        sc.close();
    }

    private static double dichotomy(double num) {
        double right, left, mid = 0.0;
        //一定要注意边界条件，输入的num可能是负数  将x<-1的边界范围定为[x,1]，x>1的边界范围定为[-1,x]
        right = Math.max(1.0, num);
        left = Math.min(-1.0, num);
        while (right - left > 0.001) {
            mid = (left + right) / 2;
            if (mid * mid * mid > num) {
                //如果乘积大于num，说明立方根在mid的左侧
                right = mid;
            } else if (mid * mid * mid < num) {
                //如果乘积小于num，说明立方根在mid的右侧
                left = mid;
            } else {
                return mid;
            }
        }
        return right;
    }

    /**
     * HJ108 求最小公倍数
     * @throws Exception
     */
    public static void test108() throws Exception{
        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
        String str;
        while ((str = bf.readLine()) != null) {
            String[] strArray = str.split(" ");
            int a = Integer.parseInt(strArray[0]);
            int b = Integer.parseInt(strArray[1]);
            int min = Math.min(a, b);
            int max = Math.max(a, b);
            for (int i = 1; i <= min; i++) {
                int tempNum = max * i;
                if (tempNum % min == 0) {
                    System.out.println(tempNum);
                    break;
                }
            }
        }
    }


}
