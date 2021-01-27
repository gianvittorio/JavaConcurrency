package com.gianvittorio.concurrency.lesson5;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class FeedCatsDemo {
    public static void main(String[] args) {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);

        List<String> initialElements = List.of("Ella", "Eclair", "Larry", "Felix");

        List<String> cats = new CopyOnWriteArrayList<>(initialElements);

        Runnable feedCats = () -> {
            try {
                cats.stream()
                        .forEach(cat -> System.out.println("Feeding " + cat));
                System.out.println();
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
        scheduler.scheduleAtFixedRate(feedCats, 0, 100, TimeUnit.MILLISECONDS);


        AtomicInteger communiyCatNumber = new AtomicInteger(1);

        Runnable adoptCommunity = () -> {
            try {
                cats.add("Community cat " + communiyCatNumber.getAndIncrement());
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
        scheduler.scheduleAtFixedRate(adoptCommunity, 0, 1000, TimeUnit.MILLISECONDS);
    }
}
