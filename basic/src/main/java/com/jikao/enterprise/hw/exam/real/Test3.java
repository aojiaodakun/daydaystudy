package com.jikao.enterprise.hw.exam.real;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

/**
 * 过滤组合字符串
 * 78
 * ux
 *
 * uw,vw,vx,
 *
 * 78
 * x
 *
 * uw,vw,
 */
public class Test3 {

    private static Map<Integer, List<Character>> NUMBER_CHARLIST_MAP = new HashMap(){
        {
            put(0, Arrays.asList('a','b','c'));
            put(1, Arrays.asList('d','e','f'));
            put(2, Arrays.asList('g','h','i'));
            put(3, Arrays.asList('j','k','l'));
            put(4, Arrays.asList('m','n','o'));
            put(5, Arrays.asList('p','q','r'));
            put(6, Arrays.asList('s','t'));
            put(7, Arrays.asList('u','v'));
            put(8, Arrays.asList('w','x'));
            put(9, Arrays.asList('y','z'));
        }
    };


    public static void main(String[] args) throws Exception{
        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
        String str;
        while ((str = bf.readLine()) != null) {
            // 数字,小于等于5
            int[] numArray = new int[str.length()];
            char[] charArray = str.toCharArray();
            for (int i = 0; i < charArray.length; i++) {
                numArray[i] = charArray[i] - 48;
            }
            // 屏幕字符
            char[] hiddenCharArray = bf.readLine().toCharArray();
            if (numArray.length == 2) {
                num2(numArray, hiddenCharArray);
                continue;
            } else if(numArray.length == 3) {
                num3(numArray, hiddenCharArray);
                continue;
            }

        }

    }

    private static void num2(int[] numArray, char[] hiddenCharArray){
        List<Character> list0 = NUMBER_CHARLIST_MAP.get(numArray[0]);
        List<Character> list1 = NUMBER_CHARLIST_MAP.get(numArray[1]);

        List<String> resultList = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list0.size(); i++) {
            char c0 = list0.get(i);
            sb.append(c0);
            for (int j = 0; j < list1.size(); j++) {
                char c1 = list1.get(j);
                sb.append(c1);
                String tempResult = sb.toString();
                if (!checkDistance(hiddenCharArray, tempResult)) {
                    resultList.add(tempResult);
                }
                sb.deleteCharAt(sb.length()-1);
            }
            sb.deleteCharAt(sb.length()-1);
        }
        resultList.stream().forEach(str ->{
            System.out.print(str+",");
        });
    }

    private static void num3(int[] numArray, char[] hiddenCharArray){
        List<String> resultList = new ArrayList<>();


        List<Character> allList = new ArrayList<>();
        List<Character> list0 = NUMBER_CHARLIST_MAP.get(numArray[0]);
        int start0 = 0;
        int end0 = list0.size();
        List<Character> list1 = NUMBER_CHARLIST_MAP.get(numArray[1]);
        int start1 = list0.size();
        int end1 = start1 + list1.size();
        List<Character> list2 = NUMBER_CHARLIST_MAP.get(numArray[2]);
        int start2 = list0.size() + list1.size();
        int end2 = start2 + list2.size();
        allList.addAll(list0);
        allList.addAll(list1);
        allList.addAll(list2);

        StringBuilder sb = new StringBuilder();
        for (int i = start0; i < end0; i++) {
            char c0 = list0.get(i);
            sb.append(c0);
            for (int j = start1; j < end1; j++) {
                char c1 = list1.get(j-start1);
                sb.append(c1);
                for (int k = start2; k < end2; k++) {
                    char c2 = list1.get(k-start2);
                    sb.append(c2);
                    String tempResult = sb.toString();
                    if (!checkDistance(hiddenCharArray, tempResult)) {
                        resultList.add(tempResult);
                    }
                    sb.deleteCharAt(sb.length()-1);
                }
                sb.deleteCharAt(sb.length()-1);
            }
            sb.deleteCharAt(sb.length()-1);
        }
        resultList.stream().forEach(str ->{
            System.out.print(str+",");
        });
    }

    private static void num3_bak(int[] numArray, char[] hiddenCharArray){
        List<Character> list0 = NUMBER_CHARLIST_MAP.get(numArray[0]);
        List<Character> list1 = NUMBER_CHARLIST_MAP.get(numArray[1]);
        List<Character> list2 = NUMBER_CHARLIST_MAP.get(numArray[2]);

        List<String> resultList = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list0.size(); i++) {
            char c0 = list0.get(i);
            sb.append(c0);
            for (int j = 0; j < list1.size(); j++) {
                char c1 = list1.get(j);
                sb.append(c1);
                for (int k = 0; k < list2.size(); k++) {
                    char c2 = list2.get(k);
                    sb.append(c2);
                    String tempResult = sb.toString();
                    if (!checkDistance(hiddenCharArray, tempResult)) {
                        resultList.add(tempResult);
                    }
                    sb.deleteCharAt(sb.length()-1);
                }
                sb.deleteCharAt(sb.length()-1);
            }
            sb.deleteCharAt(sb.length()-1);
        }
        resultList.stream().forEach(str ->{
            System.out.print(str+",");
        });
    }

    private static boolean checkDistance(char[] hiddenCharArray, String str) {
        boolean[] visited = new boolean[hiddenCharArray.length];
        for (int i = 0; i < hiddenCharArray.length; i++) {
            if (str.indexOf(hiddenCharArray[i]) != -1) {
                visited[i] = true;
            }
        }
        int count = visited.length;
        for (int i = 0; i < visited.length; i++) {
            if (visited[i]) {
                count--;
            }
        }
        if (count == 0) {
            return true;
        }
        return false;
    }


}
