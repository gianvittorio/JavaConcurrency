package com.gianvittorio.concurrency.lesson1;

import java.util.concurrent.ForkJoinPool;

public class ParallelAdder<T extends Integer> {
    private final ForkJoinPool pool;

    private final T[] nums;

    public ParallelAdder(T[] nums, ForkJoinPool pool) {
        this.nums = nums;
        this.pool = pool;
    }

    T sum(Summable<T> summable) {
        return summable.sum(pool);
    }
}