package com.jikao.nowcoder.hw;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class NowCoderTest0_6 {

    public static void main(String[] args) throws Exception {
//        test26();
//        test27();
//        test28();
//        test29();
        test30();
    }

    /**
     * HJ26 字符串排序
     *
     * 输入：
     * A Famous Saying: Much Ado About Nothing (2012/8).
     *
     * 输出：
     * A aaAAbc dFgghh: iimM nNn oooos Sttuuuy (2012/8).
     */
    public static void test26() throws Exception{
        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
        String str;
        while((str = bf.readLine())!=null){
            char[] charArray = str.toCharArray();
            char[] resultArray = Arrays.copyOf(charArray, charArray.length);
            boolean[] flag = new boolean[charArray.length];
            int index = 0;
            for (int i = 65; i < 91; i++) {
                for (int j = 0; j < charArray.length; j++) {
                    if (flag[j]) {
                        continue;
                    }
                    char c = charArray[j];
                    int cInt = c;
                    // 空格
                    if (cInt == 32) {
                        flag[j] = true;
                        continue;
                    }
                    if ((cInt == i || cInt ==i+32)) {
                        flag[j] = true;
                        while (!((resultArray[index] >=65 && resultArray[index] < 91) ||
                                (resultArray[index] >=97 && resultArray[index] < 123))) {
                            index++;
                        }
                        resultArray[index] = c;
                        index++;
                    }
                }
            }
            System.out.println(new String(resultArray));
        }
    }

    /**
     * (3) HJ27.查找兄弟单词
     *
     * 输入：
     * 3 abc bca cab abc 1
     *
     * 输出：
     * 2
     * bca
     *
     * ----
     * 输入：
     * 6 cab ad abcd cba abc bca abc 1
     *
     * 输出：
     * 3
     * bca
     *
     * 说明：
     * abc的兄弟单词有cab cba bca，所以输出3
     * 经字典序排列后，变为bca cab cba，所以第1个字典序兄弟单词为bca
     * @throws Exception
     */
    public static void test27() throws Exception{
        BufferedReader bf=new BufferedReader(new InputStreamReader(System.in));
        String s;
        while((s=bf.readLine())!=null){
            // 将输入的字符串分割成字符串数组
            String[] words=s.split(" ");
            // 待查找单词
            String str=words[words.length-2];
            // 兄弟单词表里的第k个兄弟单词
            int k=Integer.parseInt(words[words.length-1]);
            // 存放兄弟单词表
            ArrayList<String> broWords=new ArrayList<>();
            // 遍历输入的单词
            for (int i = 1; i < words.length-2; i++) {
                // 不相等且长度相同
                if((!words[i].equals(str)) && words[i].length()==str.length()) {
                    char[] chStr=str.toCharArray();
                    char[] word=words[i].toCharArray();
                    int temp=0;
                    for (int j = 0; j < chStr.length; j++) {
                        for (int j2 = 0; j2 < word.length; j2++) {
                            if (word[j]==chStr[j2]) {
                                chStr[j2]='0';
                                temp++;
                                break;
                            }
                        }
                    }
                    if (temp==chStr.length) {
                        broWords.add(words[i]);
                    }
                }
            }
            System.out.println(broWords.size());
            if(k>0 && k<=broWords.size()) {
                Collections.sort(broWords);
                System.out.println(broWords.get(k-1));
            }
        }
    }


    /**
     * HJ28 素数伴侣
     *
     * 输入:
     * 有一个正偶数 n ，表示待挑选的自然数的个数。后面给出 n 个具体的数字。
     *
     * 输出:
     * 输出一个整数 K ，表示你求得的“最佳方案”组成“素数伴侣”的对数。
     *
     * 数据范围： 1≤n≤100  ，输入的数据大小满足 2≤val≤30000
     *
     * 输入：
     * 4
     * 2 5 6 13
     *
     * 输出：
     * 2
     * @throws Exception
     */
    public static void test28() throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String line = null;
        while((line = br.readLine()) != null) {
            int count = Integer.parseInt(line);
            String[] elements = br.readLine().split(" ");
            int[] nums = new int[count];
            int oddCount = 0;
            int index = 0;
            for(String ele : elements) {
                nums[index] = Integer.parseInt(ele);
                if(nums[index] % 2 == 1) {
                    oddCount++;    //记录奇数个数
                }
                index++;
            }

            int[] oddNums = new int[oddCount];
            int[] evenNums = new int[count - oddCount];
            int oddIndex = 0;
            int evenIndex = 0;
            //奇偶分离
            for(int num : nums) {
                if(num % 2 == 0) {
                    evenNums[evenIndex++] = num;
                } else {
                    oddNums[oddIndex++] = num;
                }
            }

            int pairCount = 0;
            int[] evenPair = new int[evenIndex];
            for(int i = 0; i < oddIndex; i++) {
                boolean[] used = new boolean[evenIndex];
                if(findPair(i, oddNums, evenNums, evenPair, used)){
                    pairCount++;
                }
            }
            System.out.println(pairCount);
        }
    }
    /**
     偶数+奇数才可能是素数
     */
    private static boolean findPair(int oddIndex, int[] oddNums, int[] evenNums, int[] evenPair, boolean[] used) {
        for(int i = 0;i < evenNums.length; i++) {
            if(!used[i] && isP(oddNums[oddIndex] + evenNums[i])) {
                used[i] = true;
                if(evenPair[i] == 0 || findPair(evenPair[i] - 1,oddNums,evenNums,evenPair,used)) {
                    evenPair[i] = oddIndex + 1;
                    return true;
                }
            }
        }
        return false;
    }

    //见过比较经典的思路
    private static boolean isP(int num) {
        if(num <= 3) {
            return num > 1;    // 2,3都是素数
        }
        //6*n+2;6*n+3;6*n+4;6*n等都不是素数;可过滤掉2/3的判断
        if(num % 6 != 1 && num % 6 !=5) {
            return false;
        }
        double sqrt = Math.sqrt(num);
        for (int i = 5; i < sqrt; i += 6) {
            //只保留2类数据num % 6 == 1 num % 6 == 5
            if (num % i == 0 || num % (i + 2) == 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * HJ29 字符串加解密
     *
     * @throws Exception
     */
    public static void test29() throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String str1 = br.readLine();
        String str2 = br.readLine();

        StringBuilder sb1 = new StringBuilder();
        char[] charArray1 = str1.toCharArray();
        for (int i = 0; i < charArray1.length; i++) {
            char tempC = charArray1[i];
            char c;
            if (tempC>=48 && tempC < 58){
                c = numberCode(tempC, true);
            } else {
                c = zimuCode(tempC, true);
            }
            sb1.append(c);
        }

        StringBuilder sb2 = new StringBuilder();
        char[] charArray2 = str2.toCharArray();
        for (int i = 0; i < charArray2.length; i++) {
            char tempC = charArray2[i];
            char c;
            if (tempC>=48 && tempC < 58){
                c = numberCode(tempC, false);
            } else {
                c = zimuCode(tempC, false);
            }
            sb2.append(c);
        }

        System.out.println(sb1);
        System.out.println(sb2);
    }

    private static char zimuCode(char c, boolean encode) {
        // 大写字母
        if (c >=65 && c<91) {
            if (encode) {
                if (c == 'Z') {
                    return 'a';
                }
                return (char)(c+33);
            } else {
                if (c == 'A') {
                    return 'z';
                }
                return (char)(c+31);
            }
        } else if(c >=97 && c<123) {
            // 小写字母
            if (encode) {
                if (c == 'z') {
                    return 'A';
                }
                return (char)(c-31);
            } else {
                if (c == 'a') {
                    return 'Z';
                }
                return (char)(c-33);
            }
        }
        return 'a';
    }

    private static char numberCode(char c, boolean encode) {
        int cInt = c;
        if (encode) {
            if (cInt == 57) {
                return 48;
            }
            cInt++;
            return (char)cInt;
        } else {
            if (cInt == 48) {
                return 57;
            }
            cInt--;
            return (char)cInt;
        }
    }

    /**
     * HJ30 字符串合并处理
     * @throws Exception
     */
    public static void test30() throws Exception {
        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
        String s = null;
        while((s = bf.readLine())!=null){
            String[] str = s.split(" ");
            s = str[0] + str[1];
            char[] array = sort(s);
            System.out.println(transform(array));
        }
    }

    public static char[] sort(String s){
        char[] array = s.toCharArray();
        int i,j;
        for(i=2;i<array.length;i+=2){
            if(array[i] < array[i-2]){
                char tmp = array[i];
                for(j=i;j>0 && array[j-2] > tmp; j-=2){
                    array[j] = array[j-2];
                }
                array[j] = tmp;
            }
        }
        for(i=3;i<array.length;i+=2){
            if(array[i] < array[i-2]){
                char tmp = array[i];
                for(j=i;j>1 && array[j-2]>tmp;j-=2){
                    array[j] = array[j-2];
                }
                array[j] = tmp;
            }
        }
        return array;
    }

    public static String transform(char[] array){
        for(int i=0;i<array.length;i++){
            int num = -1;
            if(array[i] >= 'A' && array[i] <= 'F'){
                num = array[i]-'A'+10;
            }else if(array[i] >= 'a' && array[i] <= 'f'){
                num = array[i]-'a'+10;
            }else if(array[i] >= '0' && array[i] <= '9'){
                num = array[i]-'0';
            }

            if(num != -1){ // 需要转换
                num = (num&1)*8 + (num&2)*2 + (num&4)/2 + (num&8)/8;
                if(num<10){
                    array[i] = (char)(num+'0');
                }else if(num<16){
                    array[i] = (char)(num-10+'A');
                }
            }
        }
        return new String(array);
    }

}
