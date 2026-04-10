package com.jikao.nowcoder.hw;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

/**
 * TODO
 * 61、动态规划
 * 64、MP3光标位置
 * 65、动态规划
 */
public class NowCoderTest0_13 {

    public static void main(String[] args) throws Exception {
//        test61();
//        test62();
//        test63();
//        test64();
        test65();
    }

    /**
     * HJ61 放苹果
     * @throws Exception
     */
    public static void test61() throws Exception{
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            int m = scanner.nextInt();
            int n = scanner.nextInt();
            System.out.println(count1(m, n));
        }
    }

    // 递归功能：当前持有m个苹果，有n个盘子可供存放，返回摆放方案数
    private static int count1(int m, int n) {
        // 递归出口
        // 当苹果数m=0， 此时表示什么都不做，输出1种方案，再递归下去m<0，题目要求m>=0
        // 当盘子只有一个时，剩下的苹果m只能全部摆放这个盘子，递归返回1种方案，再递归下去n==0, 题目要求n>=1
        if(m == 0 || n == 1) {
            return 1;
        }
        // 当盘子数大于苹果数，一定有n-m个盘子空着，而且每个盘子都一样，因此count(m,n)==count(m,n-1)
        if(n > m) {
            return count1(m, n-1);
        } else {
            // 当盘子数<=苹果数，有两种情况：
            // 1.至少有一个盘子可以不放，因此count(m, n-1)
            // 2.至少每个盘子都有一个苹果，摆放后苹果数为(m-n)，因此count(m-n, n)
            return count1(m, n - 1) + count1(m - n, n);
        }
    }
    // 动态规划
    private static int count2(int m, int n) {
        // 持有i个苹果，有j个盘子可以存放苹果，总共有 dp[i][j]种方法
        int[][] dp = new int[m+1][n+1];
        // base case：没有苹果，只有一种摆放方法，可以作为下面递推的终止结果
        for(int j = 0; j <= n; j++) {
            dp[0][j] = 1;
        }
        for(int i = 1; i <= m; i++) {
            for(int j = 1; j <= n; j++) {
                if(i < j) {
                    // 苹果数 < 盘子数，有空盘，
                    // 则忽略一个盘子，在n-1个放苹果，一直递推到n==1，有一种摆法
                    dp[i][j] = dp[i][j-1];
                } else {
                    // 苹果数 >= 盘子数，可以看作没有空盘
                    // 则可以选择忽略一个盘子，如上边做法
                    // 还可以选择每个盘子放一个苹果，即苹果数剩下i-j,继续递推直到j==1
                    dp[i][j] = dp[i][j-1] + dp[i-j][j];
                }
            }
        }
        return dp[m][n];
    }

    /**
     * HJ62 查找输入整数二进制中1的个数
     * @throws Exception
     */
    public static void test62() throws Exception{
        Scanner in = new Scanner(System.in);
        // 注意 hasNext 和 hasNextLine 的区别
        while (in.hasNextInt()) { // 注意 while 处理多个 case
            int a = in.nextInt();
            String str = Integer.toBinaryString(a);
            char[] charArray = str.toCharArray();
            int count = 0;
            for (int i = 0; i < charArray.length; i++) {
                if (charArray[i] == '1') {
                    count++;
                }
            }
            System.out.println(count);
        }
    }

    /**
     * HJ63 DNA序列
     * @throws Exception
     */
    public static void test63() throws Exception{
        Scanner sc = new Scanner(System.in);
        while(sc.hasNext()){
            String str = sc.nextLine();
            int size = sc.nextInt();
            char[] charArray = str.toCharArray();

            int maxCount = 0;
            int maxIndex = 0;
            for (int i = 0; i < str.length() - size; i++) {
                int count = 0;
                for (int j = i; j < size + i; j++) {
                    char tempC = charArray[j];
                    if (tempC == 'G' || tempC == 'C') {
                        count++;
                    }
                }
                if (maxCount == 0) {
                    maxCount = count;
                } else {
                    if (maxCount < count) {
                        maxCount = count;
                        maxIndex = i;
                    }
                }
            }
            System.out.println(str.substring(maxIndex, maxIndex+size));
        }
    }


    /**
     * HJ64 MP3光标位置
     * @throws Exception
     */
    public static void test64() throws Exception{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String str;
        while ((str = br.readLine()) != null) {
            int num = Integer.parseInt(str);
            // 记录歌曲索引的加减
            int index = 1;
            // 记录光标在本页移动位置
            int count = 1;
            String string = br.readLine();
            // 记录操作序列
            char[] opr = string.toCharArray();
            // 歌曲小于4时执行如下
            if (num <= 4) {
                for (int i = 0; i < opr.length; i++) {
                    if (opr[i] == 'U' && index == 1) {
                        index = num;
                    } else if (opr[i] == 'U') {
                        index = index - 1;
                    } else if (opr[i] == 'D' && index == num) {
                        index = 1;
                    } else if (opr[i] == 'D') {
                        index = index + 1;
                    }
                }
                for(int i = 1; i <= num; i++){
                    System.out.print(i + " ");
                }
                System.out.println();
                System.out.println(index);
                continue;
            }

            // 歌曲大于4时执行如下
            for (int i = 0; i < opr.length; i++) {
                // 歌曲序号为1，且执行上翻操作
                if (opr[i] == 'U' && index == 1) {
                    index = num;
                    count = 4;
                    // 序号不为1时执行上翻操作
                } else if (opr[i] == 'U') {
                    index = index - 1;
                    count--;
                    // 光标位置小于1，仍将它置为1
                    if (count < 1) {
                        count = 1;
                    }
                    // 歌曲序号为最后且执行下翻操作
                } else if (opr[i] == 'D' && index == num) {
                    index = 1;
                    count = 1;
                    // 序号不为最后时执行下翻操作
                } else if (opr[i] == 'D') {
                    index = index + 1;
                    count++;
                    // 光标位置大于4，仍将它置为4
                    if (count > 4) {
                        count = 4;
                    }
                }
            }
            if (count == 1) {
                System.out.println(index + " " + (index + 1) + " " + (index + 2) + " " + (index + 3));
            } else if (count == 2) {
                System.out.println((index - 1) + " " + index + " " + (index + 1) + " " + (index + 2));
            } else if (count == 3) {
                System.out.println((index - 2) + " " + (index - 1) + " " + index + " " + (index + 1));
            } else if (count == 4) {
                System.out.println((index - 3) + " " + (index - 2) + " " + (index - 1) + " " + index);
            }
            System.out.println(index);

        }
    }

    /**
     * HJ65 查找两个字符串a,b中的最长公共子串
     * @throws Exception
     */
    public static void test65() throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String str;
        while((str = br.readLine())!=null){
            String ss = br.readLine();
            if(str.length()<ss.length()){
                System.out.println(res(str,ss));
            }else{
                System.out.println(res(ss,str));
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

    private static void my65() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String str1;
        while ((str1 = br.readLine()) != null) {
            String str2 = br.readLine();
            String longStr;
            String shortStr;
            if (str1.length() >= str2.length()) {
                longStr = str1;
                shortStr = str2;
            } else {
                longStr = str2;
                shortStr = str1;
            }

            int max = 0;
            int maxIndex = -1;

            int index = -1;
            int count = 0;
            char[] shortCharArray = shortStr.toCharArray();
            int lastIndex = 0;
            boolean isLast = false;
            for (int i = 0; i < shortCharArray.length; i++) {
                for (int j = i+1; j <= shortCharArray.length; j++) {
                    String tempStr = shortStr.substring(i, j);
                    if (longStr.contains(tempStr)) {
                        if (index == -1) {
                            index =i;
                        }
                        count++;
                        if (i == shortCharArray.length -1 || j == shortCharArray.length -1){
                            isLast = true;
                        }
                    } else {
                        if (count > max) {
                            maxIndex = index;
                            max = count;
                        }
                        count = 0;
                        break;
                    }
                }
                if (isLast) {
                    if (count > max) {
                        maxIndex = index;
                        max = count;
                    }
                    break;
                }
                index = -1;
            }
            if (maxIndex != -1) {
                System.out.println(shortStr.substring(maxIndex, maxIndex+max));
            }
        }
    }


}
