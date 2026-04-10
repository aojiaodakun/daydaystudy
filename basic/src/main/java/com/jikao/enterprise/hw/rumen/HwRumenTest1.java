package com.jikao.enterprise.hw.rumen;

import com.jikao.nowcoder.hw.NowCoderTest0_1;
import com.jikao.nowcoder.hw.NowCoderTest0_2;

import java.util.HashMap;
import java.util.Map;

/**
 * 1.入门题（5题）
 * (1) 输入处理（重要）：HJ5.进制转换
 * (2) 排列组合：(牛客搜索)NC61.两数之和
 * (3) 快速排序：HJ3.明明的随机数
 * (4) 哈希表：HJ10.字符个数统计
 * (5) 递归：NC68.跳台阶。——牛客
 */
public class HwRumenTest1 {

    public static void main(String[] args) throws Exception {
        test1();
//        test2();
//        test3();
//        test4();
        test5();
    }

    /**
     * (1) 输入处理（重要）：HJ5.进制转换
     */
    private static void test1() throws Exception{
        NowCoderTest0_1.test5();
    }

    /**
     * (2) 排列组合：(牛客搜索)NC61.两数之和
     * 输入：nums = [2,7,11,15], target = 9
     * 输出：[0,1]
     * 解释：因为 nums[0] + nums[1] == 9 ，返回 [0, 1] 。
     *
     * 来源：力扣（LeetCode）
     * 链接：https://leetcode.cn/problems/two-sum
     * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
     */
    private static void test2() throws Exception{
        int[] nums = new int[]{2,7,11,15};
        int targer = 9;
        int[] resultArray = twoSum(nums, targer);
        System.out.println(resultArray);
    }

    private static int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> hashMap = new HashMap<>();
        for (int i = 0; i < nums.length; ++i) {
            if (hashMap.containsKey(target - nums[i])) {
                return new int[]{hashMap.get(target - nums[i]), i};
            }
            hashMap.put(nums[i], i);
        }
        return new int[0];
    }

    /**
     * (3) 快速排序：HJ3.明明的随机数
     * @throws Exception
     */
    private static void test3() throws Exception{
        NowCoderTest0_1.test3();
    }


    /**
     * (4) 哈希表：HJ10.字符个数统计
     * @throws Exception
     */
    private static void test4() throws Exception{
        NowCoderTest0_2.test10();
    }

    /**
     * (5) 递归：NC68.跳台阶
     * 题目：https://www.nowcoder.com/practice/8c82a5b80378478f9484d87d1c5f12a4?tpId=117&&tqId=34990&rp=1&ru=/ta/job-code-high&qru=/ta/job-code-high/question-ranking
     * https://blog.csdn.net/qq_39240270/article/details/87185711
     * @throws Exception
     */
    private static void test5() throws Exception{
        int i1 = jumpFloor(5);
        int i2 = jumpFloor(6);
        System.out.println(i1);
        System.out.println(i2);
    }

    // 加入数组缓存已计算数据
    static int[] floorArray = new int[50];
    private static int jumpFloor(int target){
        if (target <= 2) {
            return target;
        } else if(floorArray[target] >0){
            return floorArray[target];
        } else {
            floorArray[target] = jumpFloor(target -1) + jumpFloor(target - 2);
            return floorArray[target];
        }
    }

    /**
     * 动态规划
     * @param number
     * @return
     */
    private static int jumpFloor2(int number) {
        int a = 1 , b = 1 , c = 1;
        for (int i = 2 ; i <= number ; i ++) {
            c = a+b;
            a = b;
            b = c;
        }
        return c;
    }

}
