package com.gianvittorio.concurrency.lesson5;

import java.util.HashMap;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ConcurrentHashMapDemo {
    private static final Random random = new Random();

    public static void main(String[] args) {
        //demoConcurrentHashMap();

        demoConcurrentQueue();
    }

    public static void demoConcurrentQueue() {
        ExecutorService service = Executors.newCachedThreadPool();

        Queue<String> queue = new ConcurrentLinkedQueue<>();

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
        service.submit(() -> joinQueue.accept("Yo"));
        service.submit(() -> joinQueue.accept("Daniel"));

        service.shutdown();
    }

    public static void demoConcurrentHashMap() {
        ExecutorService service = Executors.newCachedThreadPool();

        String brandNewShoes = "brand new shoes",
                cowboyShoes = "corboy shoes",
                leatherHat = "leather hat";

        ConcurrentHashMap<String, String> itemToBuyerMap = new ConcurrentHashMap<>();

        BiConsumer<? super String, ? super String> buyItemIfNotTaken = (buyer, item) -> {
            try {
                TimeUnit.MILLISECONDS.sleep(random.nextInt(1000));
                itemToBuyerMap.putIfAbsent(item, buyer);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        service.submit(() -> {
            buyItemIfNotTaken.accept("Alice", brandNewShoes);
            buyItemIfNotTaken.accept("Alice", cowboyShoes);
            buyItemIfNotTaken.accept("Alice", leatherHat);
        });

        service.submit(() -> {
            buyItemIfNotTaken.accept("Bob", brandNewShoes);
            buyItemIfNotTaken.accept("Bob", cowboyShoes);
            buyItemIfNotTaken.accept("Bob", leatherHat);
        });

        service.shutdown();
    }
}
