package com.gianvittorio.concurrency.lesson4;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;

public class WaitForGreenDemo {
    public static void main(String[] args) throws InterruptedException {
        demoWaitNotifyWithMessageQueue();
    }

    private static void waitForGreenDemo() throws InterruptedException {
        AtomicBoolean isGreenLight = new AtomicBoolean(false);
        Object lock = new Object();

        Runnable waitForGreenLightAndGo = () -> {
            System.out.println("Waiting for the green light....");

            synchronized (lock) {
                if (!isGreenLight.get()) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            System.out.println("Go!!!");
        };

        new Thread(waitForGreenLightAndGo).start();

        TimeUnit.MILLISECONDS.sleep(500);

        synchronized (lock) {
            isGreenLight.set(true);
            lock.notify();
        }
    }

    private static void demoWaitNotifyWithMessageQueue() {
        ExecutorService service = Executors.newFixedThreadPool(2);

        BrokenMessageQueueWithLock messageQueue = new BrokenMessageQueueWithLock();

        Runnable producer = () -> {
            String[] messages = {"Streets", "full of water.", "Please", "advise."};

            Arrays.stream(messages)
                    .forEach(message -> {
                        System.out.format("%s sending >> %s%n", Thread.currentThread().getName(), message);
                        messageQueue.send(message);
                        try {
                            TimeUnit.MILLISECONDS.sleep(200);
                        } catch (InterruptedException e) {
                        }
                    });
        };

        Runnable consumer = () -> {
            IntStream.range(0, 4)
                    .forEach(i -> {
                        String message = messageQueue.receive();
                        System.out.format("%s received << %s%n", Thread.currentThread().getName(), message);

                        try {
                            TimeUnit.MILLISECONDS.sleep(0);
                        } catch (InterruptedException e) {
                        }
                    });
        };

        service.execute(producer);
        service.execute(consumer);

        service.shutdown();
    }

    private static class BrokenMessageQueue implements MessageQueue {
        private static final int CAPACITY = 2;

        private final Queue<String> queue = new LinkedList<>();

        @Override
        public synchronized void send(String message) {
            while (queue.size() == CAPACITY) {
                try {
                    wait();
                } catch (InterruptedException e) {

                }
            }
            queue.add(message);
            notifyAll();
        }

        @Override
        public synchronized String receive() {
            while (queue.isEmpty()) {
                try {
                    wait();
                } catch (InterruptedException e) {

                }
            }
            String message = queue.poll();
            notifyAll();

            return message;
        }
    }

    private static class BrokenMessageQueueWithLock implements MessageQueue{
        private static final int CAPACITY = 2;

        private final Queue<String> queue = new LinkedList<>();

        private final Lock lock = new ReentrantLock();

        private final Condition queueNotEmpty = lock.newCondition();
        private final Condition queueNotFull = lock.newCondition();

        @Override
        public void send(String message) {
            lock.lock();
            try {
                while (queue.size() == CAPACITY) {
                    try {
                        queueNotFull.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                queue.add(message);
                queueNotEmpty.signalAll();
            } finally {
                lock.unlock();
            }
        }

        @Override
        public String receive() {
            String message = "";

            lock.lock();
            try {
                while (queue.isEmpty()) {
                    try {
                        queueNotEmpty.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                message = queue.poll();
                queueNotFull.signalAll();
            } finally {
                lock.unlock();
            }

            return message;
        }
    }
}
