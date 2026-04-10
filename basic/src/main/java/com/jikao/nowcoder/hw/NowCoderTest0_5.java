package com.jikao.nowcoder.hw;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * 未理解
 * 24
 */
public class NowCoderTest0_5 {

    public static void main(String[] args) throws Exception {
//        test21();
//        test22();
//        test23();
        test24();
//        test25();
    }

    /**
     * HJ21 简单密码
     *
     * 输入：
     * YUANzhi1987
     *
     * 输出：
     * zvbo9441987
     */
    public static void test21() {
        Map<Character, Integer> map = new HashMap() {
            {
                put('a', 2);
                put('b', 2);
                put('c', 2);
                put('d', 3);
                put('e', 3);
                put('f', 3);
                put('g', 4);
                put('h', 4);
                put('i', 4);
                put('j', 5);
                put('k', 5);
                put('l', 5);
                put('m', 6);
                put('n', 6);
                put('o', 6);
                put('p', 7);
                put('q', 7);
                put('r', 7);
                put('s', 7);
                put('t', 8);
                put('u', 8);
                put('v', 8);
                put('w', 9);
                put('x', 9);
                put('y', 9);
                put('z', 9);
            }
        };

        Scanner in = new Scanner(System.in);
        while (in.hasNext()) {
            String str = in.nextLine();
            StringBuilder sb = new StringBuilder();
            char[] charArray = str.toCharArray();
            for (int i = 0; i < charArray.length; i++) {
                char c = charArray[i];
                // 大写
                if (c >= 65 && c < 97) {
                    char newC;
                    if (c == 90) {
                        newC = 97;
                    } else {
                        newC = (char)((int)c +33);
                    }
                    sb.append(newC);
                } else if(c >=97) {
                    // 小写
                    sb.append(map.get(c));
                } else {
                    sb.append(c);
                }
            }
            System.out.println(sb);
        }
    }

    /**
     * HJ22 汽水瓶
     * @throws Exception
     */
    public static void test22() throws Exception{
        Scanner in = new Scanner(System.in);
        // 注意 hasNext 和 hasNextLine 的区别
        while (in.hasNextInt()) {
            int count = 0;
            int blank = in.nextInt();

            while (blank > 2) {
                int water = blank / 3;
                count += water;
                blank = blank % 3 + water;
                if (blank == 2) {
                    count++;
                    break;
                }
            }
            if (count > 0) {
                System.out.println(count);
            }
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
     * 最长子序列
     *
     * HJ24 合唱队
     * 输入：
     * 8
     * 186 186 150 200 160 130 197 200
     *
     * 输出：
     * 4
     *
     * 说明：
     * 由于不允许改变队列元素的先后顺序，所以最终剩下的队列应该为186 200 160 130或150 200 160 130
     * @throws Exception
     */
    public static void test24() throws Exception {
        method24_01();
    }

    private static void method24_01() {
        Scanner sc = new Scanner(System.in);
        while (sc.hasNext()) {
            int n = sc.nextInt();
            int[] arr = new int[n];
            for (int i = 0; i < n; i++) {
                arr[i] = sc.nextInt();
            }

            int[] left = new int[n]; //存储每个数左边小于其的数的个数
            int[] right = new int[n];//存储每个数右边小于其的数的个数
            left[0] = 1;            //最左边的数设为1
            right[n - 1] = 1;        //最右边的数设为1
            //计算每个位置左侧的最长递增
            for (int i = 0; i < n; i++) {
                left[i] = 1;
                for (int j = 0; j < i; j++) {
                    if (arr[i] > arr[j]) {   //动态规划
                        left[i] = Math.max(left[j] + 1, left[i]);
                    }
                }
            }
            //计算每个位置右侧的最长递减
            for (int i = n - 1; i >= 0; i--) {
                right[i] = 1;
                for (int j = n - 1; j > i; j--) {
                    if (arr[i] > arr[j]) {   //动态规划
                        right[i] = Math.max(right[i], right[j] + 1);
                    }
                }
            }
            // 记录每个位置的值
            int[] result = new int[n];
            for (int i = 0; i < n; i++) {
                //位置 i计算了两次 所以需要－1
                result[i] = left[i] + right[i] - 1; //两个都包含本身
            }

            //找到最大的满足要求的值
            int max = 1;
            for (int i = 0; i < n; i++) {
                max = Math.max(result[i],max);
            }
            System.out.println(n - max);
        }
    }

    private static void method24_02() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String str = null;
        while ((str = br.readLine()) != null) {
            if (str.equals("")) continue;
            int n = Integer.parseInt(str);
            int[] heights = new int[n];
            String[] str_heights = br.readLine().split(" ");
            // 当仅有一个人时，其自己组成一个合唱队，出列0人
            if (n <= 1) {
                System.out.println(0);
                continue;
            }
            for (int i = 0; i < n; i++) heights[i] = Integer.parseInt(str_heights[i]);
            // 记录从左向右的最长递增子序列和从右向左的最长递增子序列
            int[] seq = new int[n], rev_seq = new int[n];
            int[] k = new int[n];  // 用于记录以i为终点的从左向右和从右向走的子序列元素个数
            seq[0] = heights[0];  // 初始化从左向右子序列首元素为第一个元素
            int index = 1; // 记录当前子序列的长度
            for (int i = 1; i < n; i++) {
                if (heights[i] > seq[index-1]) {  // 当当前元素大于递增序列最后一个元素时
                    k[i] = index;  // 其左边元素个数
                    seq[index++] = heights[i];  // 更新递增序列
                } else {  // 当当前元素位于目前维护递增序列之间时
                    // 使用二分搜索找到其所属位置
                    int l = 0, r = index - 1;
                    while (l < r) {
                        int mid = l + (r - l) / 2;
                        if (seq[mid] < heights[i]) l = mid + 1;
                        else r = mid;
                    }
                    seq[l] = heights[i];  // 将所属位置值进行替换
                    k[i] = l;  // 其左边元素个数
                }
            }

            // 随后，再从右向左进行上述操作
            rev_seq[0] = heights[n-1];
            index = 1;
            for (int i = n - 2; i >= 0; i--) {
                if (heights[i] > rev_seq[index-1]) {
                    k[i] += index;
                    rev_seq[index++] = heights[i];
                } else {
                    int l = 0, r = index - 1;
                    while (l < r) {
                        int mid = l + (r - l) / 2;
                        if (rev_seq[mid] < heights[i]) l = mid + 1;
                        else r = mid;
                    }
                    rev_seq[l] = heights[i];
                    k[i] += l;
                }
            }

            int max = 1;
            for (int num: k)
                if (max < num) max = num;
            // max+1为最大的k
            System.out.println(n - max - 1);
        }
    }

    /**
     * (6) HJ25. 数据分类处理
     *
     * @throws Exception
     */
    public static void test25() throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String str = null;
        while ((str = br.readLine()) != null) {
            if (str.equals("")) continue;
            String[] I = str.split(" ");
            String[] temp = br.readLine().split(" ");
            long[] R = new long[Integer.parseInt(temp[0])];
            for (int i = 0; i < R.length; i++) R[i] = Long.parseLong(temp[i+1]);
            Arrays.sort(R);
            StringBuilder res = new StringBuilder();
            int count = 0;
            for (int i = 0; i < R.length; i++) {
                if (i > 0 && R[i] == R[i-1]) continue;
                String pattern = R[i] + "";
                int num = 0;
                StringBuilder index = new StringBuilder();
                for (int j = 1; j < I.length; j++) {
                    if (I[j].contains(pattern)) {
                        num++;
                        index.append(" ").append(j-1).append(" ").append(I[j]);
                    }
                }
                if (num > 0){
                    res.append(" ").append(R[i]).append(" ").append(num).append(index);
                    count+=num*2+2;
                }
            }
            System.out.println(count + res.toString());
        }
    }

}
