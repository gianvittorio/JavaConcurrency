package com.gianvittorio.pickApples.challenge;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class InvokeAllAdder implements Summable<Integer> {
    private final Integer[] nums;

    public InvokeAllAdder(Integer[] nums) {
        this.nums = nums;
    }

    @Override
    public Integer sum(ForkJoinPool pool) {
        final int availableProcessors = Runtime.getRuntime().availableProcessors(),
                maxNumberOfThreads = Math.min(3, (availableProcessors == 0) ? (2) : (availableProcessors)),
                blockSize = (nums.length + maxNumberOfThreads - 1) / (maxNumberOfThreads);

        List<Callable<Integer>> tasks = IntStream.iterate(0, i -> i + blockSize)
                .limit(nums.length)
                .mapToObj(i -> new Callable<Integer>() {
                    @Override
                    public Integer call() throws Exception {
                        return (i + blockSize <= nums.length)
                                ? (InvokeAllAdder.this.sum(nums, i, i + blockSize))
                                : (0);
                    }
                })
                .collect(Collectors.toList());

        int l = nums.length / blockSize * blockSize,
                r = l + nums.length % blockSize,
                res = InvokeAllAdder.this.sum(nums, l, r);

        List<Future<Integer>> results = pool.invokeAll(tasks);

        res += IntStream.range(0, results.size())
                .map(i -> {
                    int result = 0;
                    try {
                        result = results.get(i).get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    return result;
                })
                .sum();

        return res;
    }

    private Integer sum(Integer[] nums, int start, int end) {
        return IntStream.range(start, end)
                .map(i -> nums[i])
                .sum();
    }
}
