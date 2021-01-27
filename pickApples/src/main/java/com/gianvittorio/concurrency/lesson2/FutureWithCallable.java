package com.gianvittorio.concurrency.lesson2;

import java.util.concurrent.*;

public class FutureWithCallable {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        demoFutureWithCallable();
    }

    public static void demoFutureWithCallable() throws InterruptedException, ExecutionException {
        System.out.println();
        System.out.println("Demo Future with Callable");

        ExecutorService pool = Executors.newCachedThreadPool();

        Runnable task = () -> {
            System.out.println("Restaurant> Slicing tomatoes");
            System.out.println("Restaurant> Chopping onions");
            System.out.println("Restaurant> Spreading with tomato and sprinkle with toppings");
            System.out.println("Restaurant> Baking pizza");
            try {
                TimeUnit.MILLISECONDS.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Pizza pizza = new Pizza();

        };

        Future<?> pizzaOrderPickup = pool.submit(task);

        System.out.println("Me: Call my brother");
        TimeUnit.MILLISECONDS.sleep(200);
        System.out.println("Me: Walk the dog");

        //pizzaOrderPickup.cancel(true);
        if (!pizzaOrderPickup.isDone()) {
            System.out.println("Me: Watch a tv show");
        }

        Object pizza = pizzaOrderPickup.get();
        System.out.println("Me: Eat the pizza: " + pizza);

        pool.shutdown();

    }
}
