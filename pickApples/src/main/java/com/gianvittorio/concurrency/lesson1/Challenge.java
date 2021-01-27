package com.gianvittorio.concurrency.lesson1;

import java.util.concurrent.ForkJoinPool;

public class Challenge {
    public static void main(String[] args) {
        Integer[] nums = new Integer[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};

        int result = new ParallelAdder<Integer>(
                nums,
                ForkJoinPool.commonPool()
        )
                .sum(new RecursiveTaskAdder(nums, 0, nums.length - 1));

        System.out.println("Result: " + result);
    }
}
