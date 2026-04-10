package com.jikao.enterprise.hw.exam.real;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Scanner;

/**
 * 统计友好度最大值
 * https://blog.csdn.net/wtswts1232/article/details/130091511
 * 讲解：https://www.bilibili.com/video/BV1dg4y1M79W
 * 1 1 0 1 2 1 0
 * 3
 *
 * 95%
 *
 * 1人
 * 0没人
 * 2障碍
 *
 * 最长不变序列
 * 1111101111111111111111111111111111111110112
 */
public class Test2 {

    public static void main(String[] args) throws Exception{
        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
        String str;
        while ((str = bf.readLine()) != null) {
            String[] strArray = str.split(" ");
            int[] numArray = new int[strArray.length];
            for (int i = 0; i < strArray.length; i++) {
                numArray[i] = Integer.parseInt(strArray[i]);
            }
            int max = 0;
            for (int i = 0; i < numArray.length; i++) {
                int currInt = numArray[i];
                if (currInt == 0) {
                    int tempMax = 0;
                    // left
                    for (int j = i-1; j >=0; j--) {
                        int tempLeft = numArray[j];
                        if (tempLeft == 1) {
                            tempMax++;
                        } else {
                            break;
                        }
                    }
                    // right
                    for (int j = i+1; j < numArray.length; j++) {
                        int tempRight = numArray[j];
                        if (tempRight == 1) {
                            tempMax++;
                        } else {
                            break;
                        }
                    }
                    max = Math.max(max, tempMax);
                }
            }
            System.out.println(max);
        }

    }

    private static void boke() throws Exception{
        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
        String str;
        while ((str = bf.readLine()) != null) {
            String[] strArray = str.split(" ");
            int[] seats = new int[strArray.length];
            for (int i = 0; i < strArray.length; i++) {
                seats[i] = Integer.parseInt(strArray[i]);
            }

            int maxFriendlyValue = 0;
            int leftFriendlyValue = 0;
            int rightFriendlyValue = 0;

            for (int i = 0; i < seats.length; i++) {
                int currInt = seats[i];
                if (currInt == 1) {
                    leftFriendlyValue++;
                } else if(currInt == 2) {
                    leftFriendlyValue = 0;
                } else if(seats[i] == 0) {
                    for (int j = i+1; j < seats.length; j++) {
                        if (seats[j] == 1) {
                            rightFriendlyValue++;
                        } else{
                            break;
                        }
                    }
                    if (maxFriendlyValue < leftFriendlyValue + rightFriendlyValue) {
                        maxFriendlyValue = leftFriendlyValue + rightFriendlyValue;
                    }
                    leftFriendlyValue = 0;
                    rightFriendlyValue = 0;
                }
            }
            System.out.println(maxFriendlyValue);
        }

    }


}
