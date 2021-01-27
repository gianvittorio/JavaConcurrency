package com.gianvittorio.concurrency.lesson5;

import com.gianvittorio.concurrency.lesson4.MessageQueue;
import com.gianvittorio.concurrency.lesson4.WaitForGreenDemo;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.stream.IntStream;

public class BlockingQueueDemo {
    public static void main(String[] args) throws InterruptedException {
        //demoConcurrentQueue();
        demoWaitNotifyWithMessageQueue();
    }

    public static void demoConcurrentQueue() throws InterruptedException {
        final Random random = new Random();

        final ExecutorService service = Executors.newCachedThreadPool();

        BlockingQueue<String> queue = new LinkedBlockingQueue<>();

        Consumer<? super String> joinQueue = name -> {
            try {
                TimeUnit.MILLISECONDS.sleep(random.nextInt(1000));
                queue.offer(name);
            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        service.submit(() -> joinQueue.accept("Alice"));
        service.submit(() -> joinQueue.accept("Bob"));
        service.submit(() -> joinQueue.accept("Carol"));
        service.submit(() -> joinQueue.accept("Daniel"));

        Runnable dequeue = () -> {
            try {
                System.out.println("poll(): " + queue.take());
            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        service.submit(dequeue);
        service.submit(dequeue);
        service.submit(dequeue);
        service.submit(dequeue);

        service.awaitTermination(2000, TimeUnit.MILLISECONDS);

        service.shutdown();
    }

    private static void demoWaitNotifyWithMessageQueue() {
        ExecutorService service = Executors.newFixedThreadPool(2);

       MessageQueue messageQueue = new MyMessageQueue();

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

    private static class MyMessageQueue implements MessageQueue {
        private static final int CAPACITY = 2;

        private final LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<>(CAPACITY);

        @Override
        public void send(String message) {
            queue.add(message);
        }

        @Override
        public synchronized String receive() {
            try {
                return queue.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return "";
        }
    }
}
