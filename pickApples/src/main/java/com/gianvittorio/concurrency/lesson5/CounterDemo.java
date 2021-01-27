package com.gianvittorio.concurrency.lesson5;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.IntStream;

public class CounterDemo {
    public static void main(String[] args) throws InterruptedException {
        demoSynchronized();

        //demoBlockedState();
    }

    public static void demoBlockedState() {
        System.out.println();
        final Object lock = new Object();

        Runnable waitALittle = () -> {
            synchronized (lock) {
                System.out.println("Wait a little...");
                while (true) ;
            }
        };

        Thread iAmBusy = new Thread(waitALittle);
        Thread iAmBusyToo = new Thread(waitALittle);

        iAmBusy.start();
        iAmBusyToo.start();

        try {
            TimeUnit.MILLISECONDS.sleep(50);
        } catch (InterruptedException e) {

        }

        System.out.println(iAmBusy.getName() + ": " + iAmBusy.getState());
        System.out.println(iAmBusyToo.getName() + ": " + iAmBusyToo.getState());
    }

    public static void demoSynchronized() throws InterruptedException {
        int numberOfWorkers = 2;
        ExecutorService service = Executors.newFixedThreadPool(numberOfWorkers);

        Counter counter = new Counter();
        IntStream.range(0, 10_000)
                .forEach(i -> service.execute(counter::increment));

        service.awaitTermination(1000, TimeUnit.MILLISECONDS);

        System.out.println("Increment 10_1000 times: " + counter.getValue());

        service.shutdown();
    }

    public static class Counter {
        private final ReadWriteLock lock;
        private final Lock readLock, writeLock;

        private AtomicInteger value = new AtomicInteger();

        public Counter() {
            lock = new ReentrantReadWriteLock();
            readLock = lock.readLock();
            writeLock = lock.writeLock();
        }

        public int getValue() {
            return value.get();
        }

        public void increment() {
            value.getAndIncrement();
        }

        public void decrement() {
            value.getAndDecrement();
        }

    }
}
