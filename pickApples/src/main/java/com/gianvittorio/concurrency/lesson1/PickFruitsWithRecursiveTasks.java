package com.gianvittorio.concurrency.lesson1;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.stream.IntStream;

public class PickFruitsWithRecursiveTasks {
    public static void main(String[] args) {
        AppleTree[] appleTrees = AppleTree.newTreeGarden(12);
        ForkJoinPool pool = ForkJoinPool.commonPool();

        PickFruitTask task = new PickFruitTask(appleTrees, 0, appleTrees.length - 1);

        int result = pool.invoke(task);

        System.out.println();
        System.out.println("Total apples picked: " + result);;
    }

    public static class PickFruitTask extends RecursiveTask<Integer> {
        private final AppleTree[] appleTrees;
        private final int startInclusive;
        private final int endInclusive;

        private final int taskThreshold = 4;

        public PickFruitTask(AppleTree[] appleTrees, int startInclusive, int endInclusive) {
            this.appleTrees = appleTrees;
            this.startInclusive = startInclusive;
            this.endInclusive = endInclusive;
        }

        protected Integer doCompute() {
            return IntStream.rangeClosed(startInclusive, endInclusive)
                    .map(i -> appleTrees[i].pickApples())
                    .sum();
        }

        private class MyException extends Exception {}

        @Override
        protected Integer compute() {
            if (endInclusive - startInclusive < taskThreshold) {
                return doCompute();
            }

            int middlePoint = (startInclusive + endInclusive) >> 1;

            PickFruitTask leftSum = new PickFruitTask(appleTrees, startInclusive, middlePoint);
            PickFruitTask rightSum = new PickFruitTask(appleTrees, middlePoint + 1, endInclusive);

            rightSum.fork();

            return  leftSum.compute() + rightSum.join();
        }
    }

}
