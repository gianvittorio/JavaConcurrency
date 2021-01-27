package com.gianvittorio.concurrency.lesson4;

import java.util.concurrent.TimeUnit;

public class ThreadDemo {
    public static void main(String[] args) throws InterruptedException {
        demoThreadState();
    }

    private static void demoThreadState() throws InterruptedException {
        System.out.format("Main thread: %s\n", Thread.currentThread().getName());

        Thread thread = new Thread(() -> System.out.println("Hi there!"));
        System.out.println("After creation: " + thread.getState());

        thread.start();
        System.out.println("After thread.start(): " + thread.getState());

        thread.join();
        System.out.println("After join: " + thread.getState());
    }
}
