package com.jikao.nowcoder.hw;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;
import java.util.TreeSet;

/**
 * TODO
 * 77、递归+动态规划
 */
public class NowCoderTest0_16 {


    public static void main(String[] args) throws Exception {
//        test76();
//        test77();
        test80();
    }

    /**
     * HJ76 尼科彻斯定理
     */
    public static void test76() {
        Scanner in = new Scanner(System.in);
        while (in.hasNext()) {
            int inputInt = in.nextInt();
            int pow = (int) Math.pow(inputInt, 2);
            int startInt = pow - inputInt + 1;
            ;
            for (int i = 0; i < inputInt; i++) {
                if (i == inputInt - 1) {
                    System.out.print(startInt + i * 2);
                } else {
                    System.out.print(startInt + i * 2 + "+");
                }
            }

        }
    }

    public static List<String> resList = new ArrayList<>();

    public static Stack<Integer> stack = new Stack<>();

    /**
     * HJ77 火车进站
     *
     * @throws Exception
     */
    public static void test77() throws Exception {
        Scanner sc = new Scanner(System.in);
        while (sc.hasNext()) {
            int n = sc.nextInt();
            int[] nums = new int[n];
            for (int i = 0; i < n; i++) {
                nums[i] = sc.nextInt();
            }
            helper(nums, 0, 0, "");
            Collections.sort(resList);
            for (String s : resList) {
                System.out.println(s);
            }
        }
    }

    /**
     * @param nums      所有的火车
     * @param pushedNum 火车已经在栈里的数量
     * @param popNum    火车已全部出来的数量
     * @param res       单次结果
     */
    private static void helper(int[] nums, int pushedNum, int popNum, String res) {
        // 3. 火车全部都出来后，记录本次的结果，继续
        if (popNum == nums.length) {
            // 火车已全部出来,
            resList.add(res);
        }
        // 1. 得让所有火车都进栈，进栈后恢复原样
        if (pushedNum < nums.length) {
            // 进去一个，已经在栈里的数量多了一个，出来的没变，出来的结果没变
            stack.push(nums[pushedNum]);
            helper(nums, pushedNum + 1, popNum, res);
            stack.pop(); //恢复现场
        }
        // 2. 得让所有进去的火车全部出来，出来后也恢复原样
        if (!stack.empty()) {
            // 出来一个，已经在栈里的数量不动(没有新进去),出来的多了一个,结果加上出来的
            int pop = stack.pop();
            helper(nums, pushedNum, popNum + 1, res + pop + " ");
            stack.push(pop); //恢复现场
        }
    }

    /**
     * HJ80 整型数组合并
     *
     * @throws Exception
     */
    public static void test80() throws Exception {
        Scanner in = new Scanner(System.in);
        while (in.hasNextInt()) {
            TreeSet<Integer> set = new TreeSet<>();
            int a = in.nextInt();
            for (int i = 0; i < a; i++) {
                set.add(in.nextInt());
            }
            int b = in.nextInt();
            for (int i = 0; i < b; i++) {
                set.add(in.nextInt());
            }
            for (Integer integer : set) {
                System.out.print(integer);
            }
            System.out.println();
        }
    }


}
