package com.jikao.enterprise.hw.exam.real;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * 距离最远的相同数字
 * 10001000001
 */
public class Test1 {

    public static void main(String[] args) throws Exception{
        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
        String str;
        while ((str = bf.readLine()) != null) {
            int size = Integer.parseInt(str);
            int[] nums = new int[size];
            int index = 0;
            while (index < size) {
                nums[index] = Integer.parseInt(bf.readLine());
                index++;
            }
            int max=-1;
            for (int i = 0; i < nums.length; i++) {
                int left = nums[i];
                for (int j = nums.length-1; j > i; j--) {
                    int right = nums[j];
                    if (left == right) {
                        int tempMax = j-i;
                        max = Math.max(max, tempMax);
                        break;
                    }
                }
            }
            System.out.println(max);
        }

    }


}
