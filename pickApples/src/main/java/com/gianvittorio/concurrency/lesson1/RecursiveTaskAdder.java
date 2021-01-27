package com.gianvittorio.concurrency.lesson1;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.stream.IntStream;

public class RecursiveTaskAdder extends RecursiveTask<Integer> implements Summable<Integer> {
    private final int start, end;

    private static final int TASK_THRESHOLD = 4;

    private Integer[] nums;

    public RecursiveTaskAdder(Integer[] nums, int start, int end) {
        this.nums = nums;
        this.start = start;
        this.end = end;
    }

    @Override
    public Integer sum(ForkJoinPool pool) {
        return pool.invoke(this);
    }

    @Override
    protected Integer compute() {
        if (end - start < TASK_THRESHOLD) {
            return doCompute();
        }

        int mid = (start + end) >> 1;

        RecursiveTaskAdder leftSum = new RecursiveTaskAdder(nums, start, mid),
                rightSum = new RecursiveTaskAdder(nums, mid + 1, end);

        rightSum.fork();

        return leftSum.compute() + rightSum.join();
    }

    private Integer doCompute() {
        return IntStream.rangeClosed(start, end)
                .map(i -> nums[i])
                .sum();
    }
}
