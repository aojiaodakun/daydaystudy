package com.hzk.datastructure.dfs;

public class DfsTest {

    public static void main(String[] args) {
        packageProblem();

    }

    /**
     * https://blog.csdn.net/Biteht/article/details/124651895
     */
    public static void packageProblem(){
        int[] w = {1,4,3};
        int[] val = {1500,3000,2000};
        int n = val.length;// 物品个数
        int m = 4;// 背包容量

        // 放置0
        int[][] dp = new int[n+1][m+1];
        // i为物品，j为容量
        for (int i = 1; i < dp.length; i++) {
            for (int j = 1; j < dp[0].length; j++) {
                if (w[i-1] > j) {// 容量不够时，拿上一行的最优解
                    dp[i][j] = dp[i-1][j];
                } else {
                    /**
                     * 上一行的最优解
                     * 放进当前物品后的价值，+，i+1个物品到剩余容量的最优解
                     */
                    dp[i][j] = Math.max(dp[i-1][j], val[i-1] + dp[i-1][j - w[i-1]]);
                }
            }
        }

        for (int i = 0; i < dp.length; i++) {
            for (int j = 0; j < dp[0].length; j++) {
                System.out.print(dp[i][j] + " ");
            }
            System.out.println();
        }

    }


}
