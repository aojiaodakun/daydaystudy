package com.hzk.datastructure.search;


public class SearchTest {

    public static void main(String[] args) {
        int[] nums = {1,2,3,4,5,6,7,8,9};
        int target = 4;
        binarySearch(nums, target);
    }

    private static int binarySearch(int[] nums, int target) {
        // left,right,mid均为索引
        int left = 0;
        int right = nums.length - 1;
        while (left <= right) {
            int mid = left + (right - left)/2;
            if (nums[mid] == target) {
                return mid;
            } else if(nums[mid] < target) {
                left = mid+1;
            } else if(nums[mid] > target) {
                right = mid-1;
            }
        }
        return -1;
    }

}
