package com.jikao.nowcoder.hw;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Scanner;


public class NowCoderTest0_7 {

    public static void main(String[] args) throws Exception {
//        test31();
        test32();
//        test33();
//        test34();
//        test35();
    }

    /**
     * HJ31 单词倒排
     * @throws Exception
     */
    public static void test31() throws Exception{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String line;
        while ((line = br.readLine()) != null && line.length() > 0) {
            char[] array = line.trim().toCharArray();
            StringBuilder sb = new StringBuilder();
            boolean flag = false;
            int mark = -1;
            for (int i = array.length - 1; i >= 0; i--) {
                if ((array[i] >= 'A' && array[i] <= 'Z') || (array[i] >= 'a' && array[i] <= 'z')) {
                    if (mark == -1)
                        mark = i;
                    flag = true;
                } else if (flag) {
                    sb.append(array, i + 1, mark - i).append(' ');
                    mark = -1;
                    flag = false;
                }
            }
            if (flag) {
                sb.append(array, 0, mark + 1);
                System.out.println(sb);
            } else {
                System.out.println(sb.substring(0, sb.length() - 1));
            }
        }
    }

    /**
     * HJ32 密码截取
     * 与回文子串一致
     * @throws Exception
     */
    public static void test32() throws Exception{
        BufferedReader bf=new BufferedReader(new InputStreamReader(System.in));
        String str;
        //中心扩散法
        while((str=bf.readLine())!=null){
            int max=0;
            for(int i=0;i<str.length()-1;i++){
                //ABA型
                int len1=longest(str,i,i);
                //ABBA型
                int len2=longest(str,i,i+1);
                max=Math.max(max,len1>len2?len1:len2);
            }
            System.out.println(max);
        }
    }

    private static int longest(String str,int i,int j){
        while(j<str.length()&&i>=0 && str.charAt(i)==str.charAt(j)){
            i--;
            j++;
        }
        return j-i-1;
    }

    /**
     *HJ33 整数与IP地址间的转换
     * 输入描述：
     * 输入
     * 1 输入IP地址
     * 2 输入10进制型的IP地址
     *
     * 输出描述：
     * 输出
     * 1 输出转换成10进制的IP地址
     * 2 输出转换后的IP地址
     *
     * 输入：
     * 10.0.3.193
     * 167969729
     *
     * 输出：
     * 167773121
     * 10.3.3.193
     * @throws Exception
     */
    public static void test33() throws Exception{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String str = null;
        while ((str = br.readLine()) != null) {
            String[] ip = str.split("\\.");
            long num = Long.parseLong(br.readLine());
            //转10进制
            System.out.println(Long.parseLong(ip[0]) << 24 | Long.parseLong(ip[1]) << 16 |
                    Long.parseLong(ip[2]) << 8 | Long.parseLong(ip[3]));
            //转ip地址
            StringBuilder sb = new StringBuilder();
            sb.append(((num >> 24) & 255)).append(".").append(((num >> 16) & 255))
                    .append(".").append(((num >> 8) & 255)).append(".").append((num & 255));
            System.out.println(sb);
        }
    }


    /**
     * 输入：
     * aabcddd
     *
     * 输出：
     * aaddd
     * @throws Exception
     */
    public static void test23() throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String str;
        while ((str = br.readLine()) != null) {
            int[] arr = new int[26];
            StringBuilder sb = new StringBuilder();
            int n = str.length();
            char[] chars = str.toCharArray();
            for(int i = 0; i < n; i++){
                arr[chars[i] - 'a']++;
            }
            int min = 20;
            for(int i: arr){
                if(i >0 && i<min){
                    min=i;
                }
            }
            for (int i = 0; i < n; i++) {
                char c = chars[i];
                if(arr[c - 'a'] != min) {
                    sb.append(chars[i]);
                }
            }
            System.out.println(sb);
        }
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
     * HJ35 蛇形矩阵
     * @throws Exception
     */
    public static void test35() throws Exception {
        Scanner in = new Scanner(System.in);
        // 注意 hasNext 和 hasNextLine 的区别
        while (in.hasNext()) { // 注意 while 处理多个 case
            int size = in.nextInt();
            int[][] array = new int[size][size];
            array[0][0] = 1;
            for (int i = 0; i < array.length; i++) {
                for (int j = 0; j < size - i; j++) {
                    if (j == 0 && i > 0) {
                        array[i][j] = array[i-1][j] + i;
                    } else if(j > 0){
                        array[i][j] = array[i][j-1] + (i+j+1);
                    }
                }
            }
            for (int i = 0; i < array.length; i++) {
                for (int j = 0; j < size - i; j++) {
                    System.out.print(array[i][j] + " ");
                }
                System.out.println();
            }
        }


    }

}
