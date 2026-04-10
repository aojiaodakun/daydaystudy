package com.jikao.enterprise.hw.other;

import com.jikao.nowcoder.hw.NowCoderTest0_12;
import com.jikao.nowcoder.hw.NowCoderTest0_22;
import com.jikao.nowcoder.hw.NowCoderTest0_5;
import com.jikao.nowcoder.hw.NowCoderTest0_6;

import java.util.*;

/**
 * 9.其他（6题）
 * (1) *HJ108.求最小公倍数
 * (2) *HJ28.素数伴侣
 * (3) *HJ60.查找组成一个偶数最接近的两个素数
 * (4) *leetcode 994.腐烂的橘子
 * (5) leetcode 204.计数质数
 * (6) HJ25. 数据分类处理
 */
public class HwOtherTest1 {

    public static void main(String[] args) throws Exception {
        HwOtherTest1 my = new HwOtherTest1();
//        test1();
//        test2();
//        test3();
//        my.test4();
//        my.test5();
        my.test6();
    }


    /**
     * (1) *HJ108.求最小公倍数
     */
    private static void test1() throws Exception{
        NowCoderTest0_22.test108();
    }

    /**
     * (2) *HJ28.素数伴侣
     */
    private static void test2() throws Exception{
        NowCoderTest0_6.test28();
    }

    /**
     * (3) *HJ60.查找组成一个偶数最接近的两个素数
     */
    private static void test3() throws Exception{
        NowCoderTest0_12.test60();
    }

    /**
     * (4) *leetcode 994.腐烂的橘子
     *
     * 提示：
     * m == grid.length
     * n == grid[i].length
     * 1 <= m, n <= 10
     * grid[i][j] 仅为 0、1 或 2
     *
     * 输入：grid = [[2,1,1],[1,1,0],[0,1,1]]
     * 输出：4
     */
    private void test4() throws Exception{
        int[][] inputArray = new int[][] {
                {2,1,1},
                {1,1,0},
                {0,1,1}
        };
        int result1 = orangesRotting(inputArray);
        System.out.println(result1);
    }

    /**
     * dr,dc配合使用得到
     * grid[r][c]
     * 上grid[r-1][c]
     * 左grid[r][c-1]
     * 下grid[r+1][c]
     * 右grid[r][c+1]
     * 的元素
     */
    int[] dr = new int[]{-1, 0, 1, 0};
    int[] dc = new int[]{0, -1, 0, 1};

    public int orangesRotting(int[][] grid) {
        // 获取二维数组的行数row 和 列数 column
        int R = grid.length, C = grid[0].length;

        // queue : all starting cells with rotten oranges
        Queue<Integer> queue = new ArrayDeque<>();
        Map<Integer, Integer> depth = new HashMap<>();
        for (int r = 0; r < R; ++r){
            for (int c = 0; c < C; ++c){
                if (grid[r][c] == 2) {
                    int code = r * C + c;  // 转化为索引唯一的一维数组
                    queue.add(code); //存储腐烂橘子
                    depth.put(code, 0); //存储橘子变为腐烂时的时间,key为橘子的一维数组下标，value为变腐烂的时间
                }
            }
        }
        int ans = 0;
        while (!queue.isEmpty()) {
            int code = queue.remove();
            int r = code / C, c = code % C;// 行列拆解
            for (int k = 0; k < 4; ++k) {// 4个方向
                int nr = r + dr[k];
                int nc = c + dc[k];
                if (0 <= nr && nr < R && 0 <= nc && nc < C && grid[nr][nc] == 1) {
                    grid[nr][nc] = 2;
                    int ncode = nr * C + nc;// 行列组装
                    queue.add(ncode);
                    // 计次的关键 元素 grid[r][c] 的上左下右元素得腐烂时间应该一致
                    depth.put(ncode, depth.get(code) + 1);
                    ans = depth.get(ncode);
                }
            }
        }

        //检查grid，此时的grid能被感染已经都腐烂了，此时还新鲜的橘子无法被感染
        for (int[] row: grid)
            for (int v: row)
                if (v == 1)
                    return -1;
        return ans;

    }

    /**
     * (5) leetcode 204.计数质数
     * @throws Exception
     */
    private void test5() throws Exception{
        System.out.println(countPrimes(100));
//        System.out.println(isPrime(100));
    }

    public int countPrimes(int n) {
        int count = 0;
        for (int i = 2; i < n; ++i) {
            count += isPrime(i) ? 1 : 0;
        }
        return count;
    }

    public boolean isPrime(int x) {
        for (int i = 2; i * i <= x; ++i) {
            if (x % i == 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * (6) HJ25. 数据分类处理
     * @throws Exception
     */
    private void test6() throws Exception{
        NowCoderTest0_5.test25();
    }



}
