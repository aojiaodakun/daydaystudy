package com.jikao.nowcoder.hw;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

public class NowCoderTest0_9 {

    // TODO 42,43,44
    public static void main(String[] args) throws Exception {
//        test41();
//        test32();
//        test33();
//        test34();
        test45();
    }

    /**
     * HJ41 称砝码
     * 类型，重量，数量
     *
     *输入：
     * 2
     * 1 2
     * 2 1
     *
     * 输出：
     * 5
     *
     * 说明：
     * 可以表示出0，1，2，3，4五种重量。
     */
    public static void test41() throws Exception{
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String line;
        while ((line = reader.readLine()) != null) {
            int n = Integer.parseInt(line);
            String[] s1 = reader.readLine().split(" ");
            String[] s2 = reader.readLine().split(" ");
            int[] weight = new int[n];
            int[] number = new int[n];
            for (int i = 0; i < n; i++) {
                weight[i] = Integer.parseInt(s1[i]);
                number[i] = Integer.parseInt(s2[i]);
            }
            boolean[] exist = new boolean[10 * 10 * 2000];//最大值10*10*2000
            int[] arr = new int[20000];//存放可能的值,存到第几位说明有多少种可能,0位不算
            int count = 1;
            for (int i = 0; i < n; i++) {// 类型
                for (int j = 0; j < number[i]; j++) {// 数量
                    //对现有总重量数组进行遍历(新增的元素在下次j循环中加入)
                    int index = count;
                    for (int k = 0; k < index; k++) {
                        int sum = arr[k] + weight[i];
                        if (!exist[sum]) {
                            exist[sum] = true;
                            arr[count] = sum;
                            count++;
                        }
                    }
                }
            }
            System.out.println(count);
        }
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
            System.out.println(sb.toString());
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
     * 输入：
     * abc
     *
     * 输出：
     * abc00000
     * @throws Exception
     */
    public static void test4() throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String str;
        while((str = br.readLine())!=null){
            int len = str.length();
            int start = 0;
            while (len >= 8){
                System.out.println(str.substring(start, start + 8));
                start += 8;
                len -= 8;
            }
            if (len > 0) {
                char[] tmp = new char[8];
                for(int i = 0;i<8;i++){
                    tmp[i]='0';
                }
                for(int i = 0; start < str.length(); i++) {
                    tmp[i] = str.charAt(start++);
                }
                System.out.println(String.valueOf(tmp));
            }
        }
    }

    /**
     * HJ45 名字的漂亮度
     * @throws Exception
     */
    public static void test45() throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String input = null;
        while (null != (input = reader.readLine())) {
            int size = Integer.parseInt(input);
            List<String> list = new ArrayList<>(size);
            int index = 0;
            while (index < size) {
                list.add(reader.readLine());
                index++;
            }
            for (int i = 0; i < size; i++) {
                int[] zimuArray = new int[26];
                char[] charArray = list.get(i).toCharArray();
                for (int j = 0; j < charArray.length; j++) {
                    char c = charArray[j];
                    int tempC = c - 97;
                    zimuArray[tempC] = zimuArray[tempC]+1;
                }
                Arrays.sort(zimuArray);
                int max = 26;
                int sum = 0;
                for (int j = zimuArray.length-1; j >=0 ; j--) {
                    if (zimuArray[j] > 0) {
                        sum+= max * zimuArray[j];
                        max--;
                    }
                }
                System.out.println(sum);
            }
        }

    }

}
