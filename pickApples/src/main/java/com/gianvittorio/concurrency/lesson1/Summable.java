package com.gianvittorio.concurrency.lesson1;

import java.util.concurrent.ForkJoinPool;

public interface Summable<T extends Integer> {
    T sum(ForkJoinPool pool);
}
