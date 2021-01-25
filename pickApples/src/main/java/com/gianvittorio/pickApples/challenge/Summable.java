package com.gianvittorio.pickApples.challenge;

import java.util.concurrent.ForkJoinPool;

public interface Summable<T extends Integer> {
    T sum(ForkJoinPool pool);
}
