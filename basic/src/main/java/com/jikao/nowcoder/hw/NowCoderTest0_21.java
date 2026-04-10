package com.jikao.nowcoder.hw;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;


public class NowCoderTest0_21 {

    public static void main(String[] args) throws Exception {
//        test101();
//        test102();
        test103();
//        test105();
    }

    /**
     * HJ101 输入整型数组和排序标识，对其元素按照升序或降序进行排序
     * @throws Exception
     */
    public static void test101() throws Exception{
        Scanner sc = new Scanner(System.in);

        //有多次输入，且存在输入数字组成数组的情况
        while(sc.hasNext()){
            //第一行输入数组元素个数
            int arrCount = sc.nextInt();
            //第二行输入待排序的数组，每个数用空格隔开
            Integer[] arr = new Integer[arrCount];
            //录入 arrCount 个 数字，组成数组
            for(int i=0;i<arrCount;i++){
                arr[i] = sc.nextInt();
            }
            //第三行输入一个整数0或1。0代表升序排序，1代表降序排序
            int flagSort = sc.nextInt();

            if(flagSort==0){
                Arrays.sort(arr,new Comparator<Integer>(){
                    public int compare(Integer o1 ,Integer o2){
                        return o1-o2;
                    }
                });
            } else if(flagSort==1){
                Arrays.sort(arr,new Comparator<Integer>(){
                    public int compare(Integer o1 ,Integer o2){
                        return o2-o1;
                    }
                });
            }

            for(Integer m : arr){
                System.out.print(m + " ");
            }
            break;
        }
        System.out.println();
    }


    /**
     * HJ102 字符统计
     * @throws Exception
     */
    public static void test102() throws Exception{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String str;
        while((str=br.readLine())!=null){
            char[] chArr = str.toCharArray();
            int[] temp = new int[150];
            for(int i=0;i<chArr.length;i++){
                temp[chArr[i]]++;
            }
            int max = 0;
            for(int j=0;j<temp.length;j++){
                if(max<temp[j]){
                    max = temp[j];
                }
            }
            StringBuilder sb = new StringBuilder();
            while(max!=0){
                for(int j=0;j<temp.length;j++){
                    if(temp[j] == max){
                        sb.append((char)j);
                    }
                }
                max--;
            }
            System.out.println(sb.toString());
        }
    }


    /**
     * HJ103 Redraiment的走法
     * 求最长递增子序列长度
     * @throws Exception
     */
    public static void test103() throws Exception{
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String string;
        while ((string=bufferedReader.readLine())!=null){
            int num = Integer.parseInt(string);
            String[] strings = bufferedReader.readLine().split(" ");
            int[] arr = new int[num];
            for(int i=0;i<num;i++){
                arr[i] = Integer.parseInt(strings[i]);
            }
            System.out.println(helper(arr));
        }
    }

    public static int helper(int[] arr){
        if(arr.length==0) return 0;
        int[] dp = new int[arr.length];
        for(int i=0;i<arr.length;i++){
            dp[i] = 1;
        }
        for(int i=0;i<arr.length;i++){
            int right = arr[i];
            for(int j=0;j<i;j++){
                if(arr[j]<right){
                    dp[i] = Math.max(dp[i],dp[j]+1);
                }
            }
        }
        int ans = 0;
        for(int i:dp){
            ans = Math.max(ans,i);
        }
        return ans;
    }


    /**
     * HJ105 记负均正II
     * @throws Exception
     */
    public static void test105() throws Exception{
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String str = null;
        int count=0, total=0, countNot = 0;
        while((str =reader.readLine())!=null){
            int num = Integer.parseInt(str);
            if(num < 0){
                count++;
            }else {
                total+= num;
                countNot++;
            }
        }
        System.out.println(count);
        if(countNot==0){
            System.out.println(0.0);
        }else{
            long round = Math.round(total * 10.0 / countNot);
            System.out.println(round/10+"."+round%10);
        }
    }

    /**
     * HJ95 人民币转换
     * @throws Exception
     */
    public static void test95() throws Exception {
        BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in));
        String str;
        while ((str = buffer.readLine()) != null) {
            String[] str1 = str.split("\\.");
            int number1 = Integer.parseInt(str1[0]);
            int number2 = Integer.parseInt(str1[1]);
            if (number2 == 0) {
                System.out.println("人民币" + cul1(number1) + "元整");
            } else if (number1 == 0) {
                System.out.println("人民币" + cul2(str1[1]));
            } else {
                System.out.println("人民币" + cul1(number1) + "元" + cul2(str1[1]));
            }
        }
    }

    public static String cul1(int num) {
        String[] flag = {"零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖", "拾"};
        String result = null;
        if (0 <= num && num <= 10) {
            result = flag[num];
        } else if (10 < num && num < 20) {
            int x = num % 10;
            result = flag[10] + flag[x];
        } else if (20 <= num && num < 100) {
            int x = num % 10;
            int y = num / 10;
            if (x == 0) {
                result = flag[y] + "拾";
            } else {
                result = flag[y] + "拾" + flag[x];
            }
        } else if (100 <= num && num <= 1000) {
            int x = num % 100;
            int y = num / 100;
            if (x == 0) {
                result = flag[y] + "佰";
            } else if (x < 10) {
                result = flag[y] + "佰零" + cul1(x);
            } else {
                result = flag[y] + "佰" + cul1(x);
            }
        } else if (1000 <= num && num < 10000) {
            int x = num % 1000;
            int y = num / 1000;
            if (x == 0) {
                result = flag[y] + "仟";
            } else if (x < 100) {
                result = flag[y] + "仟零" + cul1(x);
            } else {
                result = flag[y] + "仟" + cul1(x);
            }
        } else if (10000 <= num && num < 100000000) {
            int x = num % 10000;
            int y = num / 10000;
            if (x == 0) {
                result = cul1(y) + "万";
            } else if (0 < x && x < 1000) {
                result = cul1(y) + "万零" + cul1(x);
            } else {
                result = cul1(y) + "万" + cul1(x);
            }

        } else if (100000000 <= num) {
            int x = num % 100000000;
            int y = num / 100000000;
            if (x == 0) {
                result = cul1(y) + "亿";
            } else if (0 < x && x < 10000000) {
                result = cul1(y) + "亿零" + cul1(x);
            } else {
                result = cul1(y) + "亿" + cul1(x);
            }
        }
        return result;
    }

    public static String cul2(String num) {
        String[] flag = {"零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖", "拾"};
        char[] charArray = num.toCharArray();
        if (charArray[0] == '0') {
            int tmp = Integer.parseInt(String.valueOf(charArray[1]));
            return flag[tmp] + "分";
        } else if (charArray[1] == '0') {
            int tmp = Integer.parseInt(String.valueOf(charArray[0]));
            return flag[tmp] + "角";
        } else {
            int x = Integer.parseInt(String.valueOf(charArray[0]));
            int y = Integer.parseInt(String.valueOf(charArray[1]));
            return flag[x] + "角" + flag[y] + "分";
        }
    }





}
