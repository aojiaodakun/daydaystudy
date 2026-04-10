package com.jikao.nowcoder.hw;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


public class NowCoderTest0_18 {


    public static void main(String[] args) throws Exception {
//        test86();
//        test87();
//        test88();
//        test89();
        test90();
    }

    /**
     * HJ86 求最大连续bit数
     */
    public static void test86() throws Exception{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String str ;
        while((str = br.readLine()) != null){
            int n = Integer.parseInt(str);
            int res = 0;
            int zere = 0;
            //一直取最后一位，
            while(n != 0){
                if(n % 2 == 1){
                    zere++;
                    if(zere > res){
                        res = zere;
                    }
                }else{
                    zere = 0;
                }
                n /= 2;
            }
            System.out.println(res);
        }
    }

    /**
     * HJ87 密码强度等级
     * @throws Exception
     */
    public static void test87() throws Exception{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String in = br.readLine().trim();
        //题目设定 1<= 字符串长度 <= 300
        System.out.println(score(in));
    }

    //根据得分输出密码强度的字符串
    public static String score(String in) {
        //总得分
        int score = scoreByLength(in) + scoreByDigit(in) + scoreByLetter(in)
                + scoreByReward(in) + scoreBySgin(in);
        if (score < 25)
            return "VERY_WEAK";
        else if (score < 50)
            return "WEAK";
        else if (score < 60)
            return "AVERAGE";
        else if (score < 70)
            return "STRONG";
        else if (score < 80)
            return "VERY_STRONG";
        else if (score < 90)
            return "SECURE";
        else
            return "VERY_SECURE";
    }

    //求长度得分
    public static int scoreByLength(String in) {
        int len = in.length();
        if (len >= 8)
            return 25;
        else if (len >= 5)
            return 10;
        else
            return 5;
    }
    //英文字母得分
    public static int scoreByLetter(String in) {
        String capital = in.replaceAll("[^A-Z]", "");
        String lower = in.replaceAll("[^a-z]", "");

        if (capital.length() == 0 && lower.length() == 0)
            return 0;
        if (capital.length() != 0 && lower.length() != 0)
            return 20;
        return 10;
    }
    //阿拉伯数字得分
    public static int scoreByDigit(String in) {
        String digit = in.replaceAll("[^0-9]", "");
        if (digit.length() == 0)
            return 0;
        else if (digit.length() == 1)
            return 10;
        else
            return 20;
    }
    //特殊字符得分
    public static int scoreBySgin(String in) {
        String sgin = in.replaceAll("[a-zA-Z0-9]", "");
        if (sgin.length() == 0)
            return 0;
        else if (sgin.length() == 1)
            return 10;
        else
            return 25;
    }
    //奖励得分
    public static int scoreByReward(String in) {
        if (scoreByDigit(in) != 0 && scoreByLetter(in) == 10 && scoreBySgin(in) != 0)
            return 3;
        if (scoreByDigit(in) != 0 && scoreByLetter(in) == 20 && scoreBySgin(in) != 0)
            return 5;
        if (scoreByDigit(in) != 0 && scoreByLetter(in) != 0)
            return 2;
        return 0;
    }

    /**
     * HJ88 扑克牌大小
     * @throws Exception
     */
    public static void test88() throws Exception{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String str;
        while ((str = br.readLine()) != null) {
            System.out.println(compare(str));
        }
    }

    public static String compare(String str) {
        String[] strings = str.split("-");
        String[] puk1 = strings[0].split(" ");
        String[] puk2 = strings[1].split(" ");
        int count1 = puk1.length;
        int count2 = puk2.length;
        // 当两组牌数不同时，进行如下比较
        // 当有一个为对王时，直接输出
        // 当有一个是炸弹时，直接输出，其余情况不可比较
        if (count1 != count2) {
            if (puk1[0].equals("joker") || puk1[0].equals("JOKER")) {
                return strings[0];
            } else if (puk2[0].equals("joker") || puk2[0].equals("JOKER")) {
                return strings[1];
            } else if (count1 == 4) {
                return strings[0];
            } else if (count2 == 4) {
                return strings[1];
            } else {
                return "ERROR";
            }
        } else {
            // 当两组牌的个数相同时，有如下判断逻辑
            // 当一组为对王时，说明其他一组为对子，直接输出对王
            // 当两组为炸弹时，比较大小
            // 当两组为顺子时，比较最小牌大小
            // 当两组为对子时，比较最大牌
            // 当两组均为单牌时，王为最大牌，比较最大牌
            if (count2 == 2) {
                if (puk1[0].equals("joker") || puk1[0].equals("JOKER")) {
                    return strings[0];
                } else if (puk2[0].equals("joker") || puk2[0].equals("JOKER")) {
                    return strings[1];
                } else {
                    return compare2(strings);
                }
            } else if (count1 == 4) {
                return compare2(strings);
            } else if (count1 == 5) {
                // 顺子比较最小的
                return compare2(strings);
            } else if (count1 == 3) {
                // 三个比较
                return compare2(strings);
            } else if (count1 == 1) {
                return compare2(strings);
            } else {
                return "ERROR";
            }
        }
    }

    public static String compare2(String[] str) {
        String string = "345678910JQKA2jokerJOKER";
        String[] puk1 = str[0].split(" ");
        String[] puk2 = str[1].split(" ");
        if (string.indexOf(puk1[0]) > string.indexOf(puk2[0])) {
            return str[0];
        } else {
            return str[1];
        }
    }

    private static Map<String, Integer> map = new HashMap<String, Integer>(){{
        put("2", 2);put("3", 3);put("4", 4);put("5", 5);
        put("6", 6);put("7", 7);put("8", 8);put("9", 9);
        put("10", 10);put("J", 11);put("Q", 12);put("K", 13);
        put("A", 1);
    }};
    /**
     * HJ89 24点运算
     */
    public static void test89() throws Exception{
        Scanner in = new Scanner(System.in);
        String str = in.nextLine();
        if(str.contains("joker")){
            System.out.println("ERROR");
        }else{
            if(!dfs(str.split(" "), 0, "", 0)){
                System.out.println("NONE");
            }
        }
    }

    public static boolean dfs(String[] nums, int res, String exp, int n){
        for(int k = 0; k < nums.length; k++){
            String temp = nums[k];
            if(!temp.equals("")){
                nums[k] = "";    //已经使用过的牌标记为空字符串""
                int a = map.get(temp);
                if(n == 0){
                    if(dfs(nums, a,  exp + temp, n+1)||
                            dfs(nums, a,  exp + temp, n+1) ||
                            dfs(nums, a, exp + temp, n+1) ||
                            dfs(nums, a,  exp + temp, n+1) ){
                        return true;
                    }
                }else{
                    if(dfs(nums, res + a,  exp + "+" + temp, n+1) ||
                            dfs(nums, res - a,  exp + "-" + temp, n+1) ||
                            dfs(nums, res * a, exp + "*" + temp, n+1) ||
                            dfs(nums, res / a,  exp + "/" + temp, n+1) ){
                        return true;
                    }
                }
                nums[k] = temp; //恢复现场
            }
        }
        if(res == 24 && n == nums.length){
            System.out.println(exp);
            return true;
        }
        return false;
    }

    /**
     * HJ90 合法IP
     * @throws Exception
     */
    public static void test90() throws Exception{
        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
        String str = "";
        while((str = bf.readLine()) != null){
            String[] subIP = str.split("\\.");
            if (subIP.length != 4) {
                System.out.println("NO");
                break;
            }
            for(int i =0; i < subIP.length; i++){
                Integer intIP;
                try {
                    intIP = Integer.valueOf(subIP[i]);
                } catch (NumberFormatException e) {
                    System.out.println("NO");
                    break;
                }
                if(intIP >= 0 && intIP<=255){
                    if(i == subIP.length-1){
                        System.out.println("YES");
                    }
                    continue;
                } else{
                    System.out.println("NO");
                    break;
                }
            }
        }

    }

}
