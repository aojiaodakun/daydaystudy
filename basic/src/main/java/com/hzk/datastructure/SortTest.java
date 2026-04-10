package com.hzk.datastructure;

import java.util.Arrays;

public class SortTest {

    public static void main(String[] args) {
        int[] numbers = new int[]{6,2,1,9,8};
//        maopaoSort(numbers);
//        System.out.println(Arrays.toString(numbers));

        selectSort(numbers);
        System.out.println(Arrays.toString(numbers));
    }

    /**
     * 冒泡排序
     * @param numberArray
     */
    private static void maopaoSort(int[] numberArray) {
        int temp = 0;
        boolean flag = false;
        for (int i = 0; i < numberArray.length - 1; i++) {
            for (int j = 0; j < numberArray.length - 1 - i; j++) {
                if (numberArray[j] > numberArray[j+1]) {
                    flag = true;
                    temp = numberArray[j];
                    numberArray[j] = numberArray[j+1];
                    numberArray[j+1] = temp;
                }
            }
            if (!flag) {
                break;
            }
        }
    }

    /**
     * 选择排序
     * @param numberArray
     */
    private static void selectSort(int[] numberArray) {
        for (int i = 0; i < numberArray.length - 1; i++) {
            int index = i;
            for (int j = i+1; j < numberArray.length; j++) {
                if (numberArray[index] > numberArray[j]) {
                    index = j;
                }
            }
            if (i != index) {
                // 交换
                int temp = numberArray[index];
                numberArray[index] = numberArray[i];
                numberArray[i] = temp;
            }
        }
    }

}
