package com.jikao.nowcoder.hw;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;

/**
 * 71、动态规划
 */
public class NowCoderTest0_15 {

    public static void main(String[] args) throws Exception {
//        test71();
//        test72();
//        test73();
//        test74();
        test75();
    }

    /**
     * HJ71 字符串通配符
     * @throws Exception
     */
    public static void test71() throws Exception{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String str = null;
        while ((str = br.readLine()) != null) {
            char [] p = str.toCharArray();  // 通配符
            char [] s = br.readLine().toCharArray();  // 被匹配字符
            int m = s.length;
            int n = p.length;

            // 1. 将它们都转为小写的
            for (int i = 0; i < n; i++) {
                if (p[i] >= 'A' && p[i] <= 'Z') {
                    p[i] = (char) (p[i] - 'A' + 'a');
                }
            }

            for (int i = 0; i < m; i++) {
                if (s[i] >= 'A' && s[i] <= 'Z') {
                    s[i] = (char) (s[i] - 'A' + 'a');
                }
            }

            // 2. 动态规划，s在前，p在后
            boolean [][] dp = new boolean[m + 1][n + 1];
            dp[0][0] = true; // 空字符串匹配
            for (int j = 1; j <= n; j++) {  // 当s是空字符时，p前面的必须都是空(0)或*(>=1)时才真
                if (p[j - 1] == '*') {
                    dp[0][j] = true;
                } else {
                    break;
                }
            }

            for (int i = 1; i <= m; i++) {
                for (int j = 1; j <= n; j++) {
                    if (p[j - 1] == '?') {
                        if (check(s[i - 1])) {
                            dp[i][j] = dp[i - 1][j - 1];
                        } else {
                            dp[i][j] = false;
                        }
                    } else if (p[j - 1] == '*') {
                        if (check(s[i - 1])) {
                            dp[i][j] = dp[i - 1][j] || dp[i][j - 1];   // 匹配任意次或0次
                        } else {
                            dp[i][j] = false;
                        }
                    } else if (s[i - 1] == p[j - 1]) {
                        if (check(s[i - 1])) {
                            dp[i][j] = dp[i - 1][j - 1];
                        }
                    }
                }
            }

            System.out.println(dp[m][n]);
        }

    }
    // 合法字符。案例有问题，按题意.应该是非法字符，但系统判：p = "t?t*1*.*""  s = "txt12.xls" 为真
    public static boolean check(char letter) {
        return (letter >= 'a' && letter <= 'z') || (letter >= '0' && letter <= '9') || letter == '.';
    }

    /**
     * HJ72 百钱买百鸡问题
     * @throws Exception
     */
    public static void test72() throws Exception{
        Scanner sc = new Scanner(System.in);
        while (sc.hasNext()) {
            int num = sc.nextInt();
            int n1 = 0; // 鸡翁个数
            int n2 = 0; // 鸡母个数
            int n3 = 3; // 鸡雏个数
            for(n1 = 0; n1 < 20; n1++){
                for(n2 = 0; n2 < 33; n2++){
                    for(n3 = 0; n3 < 100; n3 = n3 + 3){
                        if(n1 + n2 + n3 == 100 && 5*n1 + 3*n2 + n3/3 == 100){
                            System.out.println(n1 + " " + n2 +  " " + n3);
                        }
                    }
                }
            }
        }
        sc.close();
    }

    /**
     * HJ73 计算日期到天数转换
     * @throws Exception
     */
    public static void test73() throws Exception{
        Scanner in=new Scanner(System.in);
        int y=in.nextInt();
        int m=in.nextInt();
        int d=in.nextInt();
        Calendar c1=Calendar.getInstance();//实例化
        c1.set(y, m-1, d);//注意月份从0开始
        System.out.println(c1.get(Calendar.DAY_OF_YEAR));
    }


    /**
     * HJ74 参数解析
     * @throws Exception
     */
    public static void test74() throws Exception{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String str;
        while ((str = br.readLine()) != null) {
            List<String> list = new ArrayList<>();
            char[] charArray = str.toCharArray();
            int startIndex=-1, endIndex =-1;
            boolean hasYinHao = false;
            for (int i = 0; i < charArray.length; i++) {
                char tempC = charArray[i];
                if (tempC == '"') {
                    if (!hasYinHao) {
                        hasYinHao = true;
                    } else {
                        hasYinHao = false;
                    }
                } else if (tempC == ' ') {
                    if (!hasYinHao) {
                        String tempStr = str.substring(startIndex, endIndex);
                        list.add(tempStr);
                        startIndex = -1;
                        endIndex = -1;
                    }
                } else {
                    if (startIndex == -1) {
                        startIndex = i;
                        endIndex = i + 1;
                    } else {
                        endIndex = i + 1;
                    }
                }
            }
            if (startIndex != -1) {
                String tempStr = str.substring(startIndex, endIndex);
                list.add(tempStr);
            }
            System.out.println(list.size());
            list.stream().forEach(e ->{
                System.out.println(e);
            });

        }
    }

    /**
     * HJ75 公共子串计算
     * @throws Exception
     */
    public static void test75() throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String str;
        while((str = br.readLine())!=null){
            String ss = br.readLine();
            if(str.length()<ss.length()){
                System.out.println(res(str,ss).length());
            }else{
                System.out.println(res(ss,str).length());
            }
        }
    }

    public static String res(String s,String c){
        char[] ch1 = s.toCharArray();
        char[] ch2 = c.toCharArray();
        int[][] ins = new int[ch1.length + 1][ch2.length + 1];
        int max = 0;
        int start = 0;
        for (int i = 0; i < ch1.length; i++) {
            for (int j = 0; j < ch2.length; j++) {
                if(ch1[i]==ch2[j]){
                    ins[i+1][j+1] = ins[i][j]+1;
                    if(ins[i+1][j+1]>max){
                        max = ins[i+1][j+1];
                        start = i-max;
                    }
                }
            }
        }
        return s.substring(start+1,start+max+1);
    }



}
