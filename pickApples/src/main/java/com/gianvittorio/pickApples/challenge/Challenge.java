package com.gianvittorio.pickApples.challenge;

import java.util.concurrent.ForkJoinPool;
import java.util.stream.IntStream;

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
