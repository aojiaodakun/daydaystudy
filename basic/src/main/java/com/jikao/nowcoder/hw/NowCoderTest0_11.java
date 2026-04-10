package com.jikao.nowcoder.hw;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Scanner;

/**
 * TODO
 * 52、动态规划
 */
public class NowCoderTest0_11 {

    public static void main(String[] args) throws Exception {
//        test51();
        test52();
//        test53();
//        test54();
//        test55();
    }

    /**
     * HJ51 输出单向链表中倒数第k个结点
     * @throws Exception
     */
    public static void test51() throws Exception{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String input = null;
        while((input = br.readLine())!=null){
            //候选人数
            int num = Integer.parseInt(input);
            String[] numStr = br.readLine().split(" ");

            int num2 = Integer.parseInt(br.readLine().trim());
            if(num2<=0||num2>numStr.length){
                System.out.println(num2);
            }else{
                System.out.println(numStr[numStr.length-num2]);
            }
        }
    }

    /**
     * HJ52 计算字符串的编辑距离
     * @throws Exception
     */
    public static void test52() throws Exception{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String str = null;
        while ((str = br.readLine()) != null) {
            System.out.println(distance(str.toCharArray(), br.readLine().toCharArray()));
        }
    }

    /*
    解题思路：
    已知str1(0~i-1)与str2(0~j)的字符距离为x，
    str1(0~i)与str2(0~j-1)的字符距离为y，
    str1(0~i-1)与str2(0~j-1)的字符距离为z，
    则str1(0~i)与str2(0~j)的字符距离为
    d(i,j)=min(x+1, y+1, z+(str1[i]==str2[j]?0:1))
     */
    private static int distance(char[] arr1, char[] arr2) {
        int[][] distances = new int[arr1.length + 1][arr2.length + 1];
        for(int i = 1; i <= arr1.length; i++) distances[i][0] = i;
        for(int i = 1; i <= arr2.length; i++) distances[0][i] = i;
        for(int i = 1; i <= arr1.length; i++) {
            for(int j = 1; j <= arr2.length; j++) {
                if(arr1[i - 1] == arr2[j - 1]) {
                    // 如果a[i] == b[j]，则说明a[i]和b[j]分别加入a，b之后不会影响levenshtein距离
                    distances[i][j] = distances[i - 1][j - 1];
                } else {
                    /**
                     * 如果a[i] != b[j]，则需要考虑3种情况的可能：
                     * a中插入字符，即lev[i][j] = lev[i-1][j] + 1;
                     * b中插入字符，即lev[i][j] = lev[i][j-1] + 1;
                     * a[i]替换成b[j]，lev[i][j] = lev[i-1][j-1] + 1;
                     */
                    distances[i][j] = min(distances[i - 1][j], distances[i][j - 1], distances[i - 1][j - 1]) + 1;
                }
            }
        }
        return distances[arr1.length][arr2.length];
    }

    private static int min(int a, int b, int c) {
        return a <= b ? (a <= c ? a : c) : (b <= c ? b : c);
    }


    //杨辉三角规律                                    行号    第一个偶数在该行第几个
//                    1                           1             -1
//                1   1   1                       2             -1
//            1   2   3   2   1                   3              2
//         1  3   6   7   6   3   1               4              3
//      1  4  10  16  19  16  10  4  1            5              2
//   1  5  15 30  45  51  45  30  15 5  1         6              4
//
//  首个偶数在该行第几个的规律： -1 -1 （2 3 2 4）···（2 3 2 4）
    /**
     * HJ53 杨辉三角的变形
     * @throws Exception
     */
    public static void test53() throws Exception{
        Scanner sc = new Scanner(System.in);
        int[] res = new int[]{2,3,2,4};
        while(sc.hasNext()){
            int n = sc.nextInt();
            if(n<=2) {
                System.out.println(-1);
                continue;
            }
            System.out.println(res[(n+1)%4]);
        }
    }


    /**
     * HJ54 表达式求值
     * 参考HJ50
     * @throws Exception
     */
    public static void test54() throws Exception{
        NowCoderTest0_10.test50();
    }

    /**
     * HJ55 挑7
     * @throws Exception
     */
    public static void test55() throws Exception {
        Scanner sc = new Scanner(System.in);
        while(sc.hasNext()){
            int count = 0;
            int n = sc.nextInt();
            for (int i = 1; i <= n; i++) {
                if (i % 7 == 0) {
                    count++;
                    continue;
                }
                char[] charArray = String.valueOf(i).toCharArray();
                for (int j = 0; j < charArray.length; j++) {
                    if (charArray[j] == '7') {
                        count++;
                        break;
                    }
                }
            }
            System.out.println(count);
        }
    }





}
