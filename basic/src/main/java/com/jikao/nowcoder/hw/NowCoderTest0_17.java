package com.jikao.nowcoder.hw;

import java.io.BufferedReader;
import java.io.InputStreamReader;


public class NowCoderTest0_17 {

    public static void main(String[] args) throws Exception {
//        test81();
//        test82();
//        test83();
//        test84();
        test85();
    }

    /**
     * HJ81 字符串字符匹配
     * @throws Exception
     */
    public static void test81() throws Exception{
        BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
        String str="";
        String str2="";
        while((str=br.readLine())!=null&&!"".equals(str)){
            str2=br.readLine();
            char[] arr=str.toCharArray();
            boolean judge=true;
            for(char c:arr){
                if(!str2.contains(String.valueOf(c))){
                    judge=false;
                    break;
                }
            }
            System.out.println(judge);
        }
        br.close();
    }


    /**
     * HJ82 将真分数分解为埃及分数
     * @throws Exception
     */
    public static void test82() throws Exception{
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String s = null;
        while ((s = bufferedReader.readLine()) != null) {
            String[] parts = s.split("/");
            // 分子
            int a = Integer.parseInt(parts[0]);
            // 分母
            int b = Integer.parseInt(parts[1]);
            StringBuilder builder = new StringBuilder(64);
            while (b % a != 0) {
                // 求商
                int q = b / a;
                // 余数
                int r = b % a;
                builder.append(1).append('/').append(q + 1).append('+');
                a = a - r;
                b = b * (q + 1);
            }
            builder.append(1).append('/').append(b / a);
            System.out.println(builder.toString());
        }
    }


    /**
     * HJ83 二维数组操作
     * @throws Exception
     */
    public static void test83() throws Exception{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String str = null;
        while ((str = br.readLine()) != null) {
            String[] strs = str.split(" ");
            //1. 行和列
            int row = Integer.parseInt(strs[0]);
            int column = Integer.parseInt(strs[1]);
            if (row >= 0 && row <= 9 && column >= 0 && column <= 9) {
                System.out.println("0");
            } else {
                System.out.println(-1);
            }

            //2.单元格的 行 列值
            String[] values = br.readLine().split(" ");
            int rowValue1 = Integer.parseInt(values[0]);
            int columnValue1 = Integer.parseInt(values[1]);

            int rowValue2 = Integer.parseInt(values[2]);
            int columnValue2 = Integer.parseInt(values[3]);

            if (rowValue1 >= 0 && rowValue1 < row && rowValue2 >= 0 && rowValue2 < row
                    && columnValue1 >= 0 && columnValue1 < column && columnValue2 >= 0 && columnValue2 < column) {
                System.out.println(0);
            } else {
                System.out.println(-1);
            }

            //3. 插入的行的值
            int insertRowValue = Integer.parseInt(br.readLine());
            if (insertRowValue >= 0 && insertRowValue < row && (row + 1) <= 9) {
                System.out.println(0);
            } else {
                System.out.println(-1);
            }
            //4. 插入的列的值
            int insertColumnValue = Integer.parseInt(br.readLine());
            if (insertColumnValue >= 0 && insertColumnValue < column && (column + 1) <= 9) {
                System.out.println(0);
            } else {
                System.out.println(-1);
            }
            //5. 运动轨迹的单元格
            strs = br.readLine().split(" ");
            int x = Integer.parseInt(strs[0]);
            int y = Integer.parseInt(strs[1]);
            if (x >= 0 && x < row && y >= 0 && y < column) {
                System.out.println(0);
            } else {
                System.out.println(-1);
            }
        }
        br.close();
    }


    /**
     * HJ84 统计大写字母个数
     * @throws Exception
     */
    public static void test84() throws Exception{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String str;
        while ((str = br.readLine()) != null) {
            int count = 0;
            char[] charArray = str.toCharArray();
            for (int i = 0; i < charArray.length; i++) {
                char c = charArray[i];
                if (c >='A' && c <= 'Z'){
                    count++;
                }
            }
            System.out.println(count);
        }
    }

    /**
     * HJ85 最长回文子串
     * @throws Exception
     */
    public static void test85() throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String str;
        while((str = br.readLine())!=null){
            char[] charArray = str.toCharArray();
            int max = 0;
            for (int i = 0; i < charArray.length; i++) {
                int a = getHuiwenLength(charArray, i,i);
                int b = getHuiwenLength(charArray, i,i+1);
                int tempMax = Math.max(a, b);
                max = Math.max(max, tempMax);
            }
            System.out.println(max);
        }
    }


    private static int getHuiwenLength(char[] chars, int i, int j){
        while (i >=0 && j<chars.length) {
            if (chars[i] == chars[j]) {
                i--;
                j++;
            } else {
                break;
            }
        }
        return j-i-1;
    }



}
