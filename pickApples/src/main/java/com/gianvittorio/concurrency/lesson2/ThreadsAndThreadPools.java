package com.gianvittorio.concurrency.lesson2;

import java.util.Random;
import java.util.concurrent.*;
import java.util.stream.IntStream;

public class ThreadsAndThreadPools {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        Runnable r = () -> System.out.println("Current thread: " + Thread.currentThread().getName());

        ExecutorService pool = Executors.newScheduledThreadPool(4);
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(4);

        ScheduledFuture<?> waterReminder = scheduler.scheduleAtFixedRate(
                () -> System.out.println("Hi there, it's time to drink a glass of water"),
                0, 1, TimeUnit.SECONDS);

        ScheduledFuture<?> exerciseReminder = scheduler.scheduleAtFixedRate(
                () -> System.out.println("Hi there, it's time to exercise"),
                0, 12, TimeUnit.SECONDS);


        Runnable canceller = () -> exerciseReminder.cancel(false);

        ThreadFactory threadFactory = new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setPriority(Thread.MAX_PRIORITY);

                return thread;
            }
        };

        ExecutorService pool2 = Executors.newFixedThreadPool(5, threadFactory);

        IntStream.range(0, 10)
                .mapToObj(i -> r)
                .forEach(pool2::submit);

        Future<Integer> randomNumber = pool.submit(() -> new Random().nextInt());
        System.out.println("Random number: " + randomNumber.get());

        pool.shutdown();
    }
}
