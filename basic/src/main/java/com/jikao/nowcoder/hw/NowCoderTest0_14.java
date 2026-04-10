package com.jikao.nowcoder.hw;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.util.Stack;

/**
 * TODO
 * 66、
 * 67、动态规划
 */
public class NowCoderTest0_14 {

    // TODO 66,67,70
    public static void main(String[] args) throws Exception {
//        test66();
//        test67();
//        test68();
//        test69();
        test70();
    }

    /**
     * hello nowcoder
     * <p>
     * 8
     */
    public static void test1() {
        Scanner in = new Scanner(System.in);
        while (in.hasNext()) {
            String str = in.nextLine();
            String[] tempArray = str.split(" ");
            int length = tempArray[tempArray.length - 1].length();
            System.out.println(length);
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
    public static void test2() throws Exception{
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
     * (5) *HJ68.成绩排序
     *
     * 输入：
     * 3
     * 0
     * fang 90
     * yang 50
     * ning 70
     *
     * 输出：
     * fang 90
     * ning 70
     * yang 50
     *
     * @throws Exception
     */
    public static void test68() throws Exception {
        BufferedReader br= new BufferedReader(new InputStreamReader(System.in));
        String str="";
        while((str=br.readLine())!=null){
            int n=Integer.parseInt(str.trim());
            int bool=Integer.parseInt(br.readLine().trim());
            String[] name =new String[n];
            int[] score=new int[n];
            for(int i=0;i<n;i++){
                str=br.readLine().trim();
                String[] temp= str.split(" ");
                name[i]=temp[0];
                score[i]=Integer.parseInt(temp[1]);
            }
            if(bool==0){  // 由高到低
                for(int i=0;i<n;i++){
                    for(int j=0;j<n-1-i;j++){
                        if(score[j+1]>score[j]){
                            String na=name[j];
                            name[j]=name[j+1];
                            name[j+1]=na;
                            int t=score[j];
                            score[j]=score[j+1];
                            score[j+1]=t;
                        }

                    }

                }
            }else{  // 由低到高
                for(int i=0;i<n;i++){
                    for(int j=0;j<n-1-i;j++){
                        if(score[j+1]<score[j]){
                            String na=name[j];
                            name[j]=name[j+1];
                            name[j+1]=na;
                            int t=score[j];
                            score[j]=score[j+1];
                            score[j+1]=t;
                        }
                    }
                }
            }
            StringBuilder sb = new StringBuilder();
            for(int i=0;i<n;i++) {
                sb.append(name[i] + " ");
                sb.append(score[i]);
                sb.append("\n");
            }
            sb.deleteCharAt(sb.length()-1);
            System.out.println(sb.toString());
        }
    }

    /**
     * HJ69 矩阵乘法
     * @throws Exception
     */
    public static void test69() throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        int x = Integer.parseInt(reader.readLine());
        int y = Integer.parseInt(reader.readLine());
        int z = Integer.parseInt(reader.readLine());

        int[][] array1 = new int[x][y];
        int[][] array2 = new int[y][z];

        int index = 0;
        while (index < x) {
            String line = reader.readLine();
            String[] tempArray = line.split(" ");
            for (int i = 0; i < tempArray.length; i++) {
                array1[index][i] = Integer.parseInt(tempArray[i]);
            }
            index++;
        }
        index = 0;
        while (index < y) {
            String line = reader.readLine();
            String[] tempArray = line.split(" ");
            for (int i = 0; i < tempArray.length; i++) {
                array2[index][i] = Integer.parseInt(tempArray[i]);
            }
            index++;
        }
        int[][] resultArray = new int[x][z];

        // ***重要，列遍历
//        for (int j = 0; j < z; j++) { // 第二个矩阵有多少列,2
//            for (int k = 0; k < y; k++) { // 3
//                int tempInt = array2[k][j];
//            }
//        }
        // 行*列
        int sum = 0;
        for (int i = 0; i < x; i++) { // 2
            for (int j = 0; j < z; j++) { // 第二个矩阵有多少列,2
                for (int k = 0; k < y; k++) { // 3
                    sum += array1[i][k] * array2[k][j];
                }
                resultArray[i][j] = sum;
                sum = 0;
            }
        }
        // 输出结果
        for (int i = 0; i < resultArray.length; i++) {
            for (int j = 0; j < resultArray[0].length; j++) {
                System.out.print(resultArray[i][j] + " ");
            }
            System.out.println();
        }

    }

    /**
     * HJ70 矩阵乘法计算量估算
     * @throws Exception
     */
    public static void test70() throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String str = null;
        while ((str = br.readLine()) != null) {
            int num = Integer.parseInt(str);
            int [][] arr = new int[num][2];

            for (int i = 0;i<num;i++) {
                String [] matrix = br.readLine().split(" ");
                arr[i][0] = Integer.parseInt(matrix[0]);
                arr[i][1] = Integer.parseInt(matrix[1]);
            }

            int n = arr.length -1;
            char [] rule = br.readLine().toCharArray();
            Stack<Integer> stack = new Stack<>();
            int sum = 0;
            for (int i = rule.length - 1; i >= 0; i--) {
                char one = rule[i];
                if (one == ')') {
                    stack.push(-1);
                } else if (one == '(') {
                    // 前一个矩阵
                    int n1 = stack.pop();
                    // 后一个矩阵
                    int n2 = stack.pop();
                    sum += arr[n1][0] * arr[n2][0] * arr[n2][1];
                    // 新矩阵
                    arr[n1][1] = arr[n2][1];
                    // 去掉一个右括号
                    stack.pop();
                    // 将新矩阵压入栈中
                    stack.push(n1);
                } else {
                    // 压入第n个矩阵
                    stack.push(n);
                    n--;
                }
            }
            System.out.println(sum);
        }
    }

}
