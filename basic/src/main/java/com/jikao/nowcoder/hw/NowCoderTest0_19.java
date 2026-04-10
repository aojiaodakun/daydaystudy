package com.jikao.nowcoder.hw;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;


public class NowCoderTest0_19 {

    public static void main(String[] args) throws Exception {
//        test91();
//        test92();
//        test93();
//        test94();
        test95();
    }

    /**
     * HJ91 走方格的方案数
     * @throws Exception
     */
    public static void test91() throws Exception{
        BufferedReader bReader = new BufferedReader(new InputStreamReader(System.in));
        String line = null;
        while ((line = bReader.readLine()) != null) {
            int n=Integer.parseInt(line.substring(0,line.indexOf(" ")));
            int m=Integer.parseInt(line.substring(line.indexOf(" ")+1));
            System.out.println(getCount(n, m));
        }
    }

    /**
     * 获取路线数量
     * @param n
     * @param m
     * @return
     */
    public static int getCount(int n,int m) {
        int[][] dp=new int[n+1][m+1];
        for(int i=0;i<n+1;i++) {
            for(int j=0;j<m+1;j++) {
                if(i==0||j==0){
                    // 边上的每一个点的可能性都是1，因为走到边上，就只能顺着边往下走了，可能性只能是1
                    dp[i][j]=1;
                }else{
                    // 往后每一个点，都是后面两个点的可能性之和，因为它可以选择任意一个点来走，可能性就是下两个点的可能性相加
                    dp[i][j]=dp[i][j-1]+dp[i-1][j];
                }
            }
        }
        // 最终加到最后一个点，可能性就是所有的路线数量
        return dp[n][m];
    }


    /**
     *
     HJ92 在字符串中找出连续最长的数字串
     * @throws Exception
     */
    public static void test92() throws Exception{
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String str = null;
        while ((str = bufferedReader.readLine()) != null) {
            StringBuilder sb = new StringBuilder();
            char[] charArray = str.toCharArray();
            int max = 0;
            int startIndex = -1;
            int endtIndex = 0;
            for (int i = 0; i < charArray.length; i++) {
                char c = charArray[i];
                if (c >='0' && c <='9') {
                    if (startIndex == -1) {
                        startIndex = i;
                        endtIndex = startIndex+1;
                    } else {
                        endtIndex++;
                    }
                } else {
                    if (startIndex != -1) {
                        int tempMax = endtIndex - startIndex;
                        if (max == tempMax && startIndex != 0) {
                            sb.append(str, startIndex, endtIndex);
                        } else if(tempMax > max) {
                            sb.setLength(0);
                            sb.append(str, startIndex, endtIndex);
                        }
                        max = Math.max(max, tempMax);
                        startIndex = -1;
                        endtIndex = 0;
                    }
                }
            }
            if (startIndex != 0) {
                int tempMax = endtIndex - startIndex;
                if (max == tempMax) {
                    sb.append(str, startIndex, endtIndex);
                } else if(tempMax > max) {
                    sb.setLength(0);
                    sb.append(str, startIndex, endtIndex);
                }
                max = Math.max(max, tempMax);
            }
            System.out.println(sb+","+max);
        }
    }


    /**
     *
     HJ93 数组分组
     * @throws Exception
     */
    public static void test93() throws Exception{
        Scanner in = new Scanner(System.in);
        while(in.hasNext()){
            //根据输入计算sum3，sum5和所有数总和sum，同时把不是5和3倍数的剩余数放入集合
            List<Integer> list = new LinkedList<>();
            int n = in.nextInt();
            int sum5=0, sum3=0, sum = 0;
            for (int i = 0; i < n; i++){
                int cur = in.nextInt();//输入
                if (cur % 5 == 0){//5倍数和
                    sum5 += cur;
                }else if (cur % 3 == 0){//3倍数和
                    sum3 += cur;
                }else{//剩余加入集合
                    list.add(cur);
                }
                sum += cur;//总和
            }

            //特例，总和不是2的倍数，不可分2份和相等的数字
            if(sum%2!=0) {
                System.out.println("false");
            } else{//否则，在剩余数中找目标数字
                int target = sum/2 - sum3;//也可以是sum/2-sum5
                System.out.println(helper(list, target, 0));
            }
        }

    }
    private static boolean helper(List<Integer> list, int target, int start){
        if (start == list.size()) return target == 0;//终止条件
        //选择start位置，递归找新目标数target-list.get(start)， 或者不选择start位置，在后面位置找target
        return helper(list, target-list.get(start), start+1) || helper(list, target, start+1);
    }


    /**
     * HJ94 记票统计
     * @throws Exception
     */
    public static void test94() throws Exception{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String line;
        while((line = br.readLine()) != null){
            int n = Integer.parseInt(line);
            String[] name = br.readLine().split(" ");
            int[] num = new int[n+1];
            int tt = 0;
            int invalidData = 0;
            int voteData = Integer.parseInt(br.readLine());
            String[] vote = br.readLine().split(" ");
            for(int i = 0; i < vote.length; i++){
                for(int j = 0; j < n; j++){
                    if(vote[i].equals(name[j])){
                        num[j]++;
                        break;
                    }
                }
            }
            for(int m = 0; m < name.length; m++){
                tt += num[m];
                System.out.println(name[m] + " : " + num[m]);
            }
            invalidData = voteData - tt;
            System.out.println("Invalid : " + invalidData);
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
