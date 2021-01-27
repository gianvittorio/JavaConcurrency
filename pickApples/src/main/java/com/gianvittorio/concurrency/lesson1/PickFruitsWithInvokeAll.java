package com.gianvittorio.concurrency.lesson1;

import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.IntStream;

/**
 * Hello world!
 */
public class PickFruitsWithInvokeAll {
    public static void main(String[] args) {
        AppleTree[] appleTrees = AppleTree.newTreeGarden(6);

        Callable<Void> applePicker1 = createApplePicker(appleTrees, 0, 2, "Alex");
        Callable<Void> applePicker2 = createApplePicker(appleTrees, 2, 4, "Alex");
        Callable<Void> applePicker3 = createApplePicker(appleTrees, 4, 6, "Carol");

        ForkJoinPool.commonPool().invokeAll(Arrays.asList(applePicker1, applePicker2, applePicker3));

        System.out.println();
        System.out.println("All fruits collected!");
    }

    public static Callable<Void> createApplePicker(AppleTree[] appleTrees, int fromIndexInclusive, int toIndexExclusive, String workerName) {
        return () -> {
            IntStream.range(fromIndexInclusive, toIndexExclusive)
                    .forEach(i -> appleTrees[i].pickApples(workerName));

            return null;
        };
    }
}
