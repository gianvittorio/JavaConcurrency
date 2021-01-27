package com.gianvittorio.concurrency.lesson2;

import java.util.concurrent.*;

public class CompletableFutureWithCallable {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newCachedThreadPool();

        CompletableFuture<String> sliceTomatoes = CompletableFuture.supplyAsync(
                () -> {
                    try {
                        TimeUnit.MILLISECONDS.sleep(10);
                    } catch (InterruptedException e) {
                    }

                    final String tomatoes = null;
                    if (tomatoes == null) {
                        throw new RuntimeException("No tomatoes");
                    }

                    System.out.println(" Restaurant> Slicing tomatoes");

                    return "tomatoes";
                },
                executorService
        )
                .handle(
                        (result, exception) -> {
                            if (result == null) {
                                System.out.println("Problems with the tomatoes :(");
                                return "";
                            }

                            return result;
                        }
                );

        CompletableFuture<String> chopOnions = CompletableFuture.supplyAsync(
                () -> {
                    System.out.println(" Restaurant> chopping onions");

                    return "onions";
                },
                executorService
        );

        CompletableFuture<String> prepIngredients = sliceTomatoes.thenCombine(chopOnions, String::concat);

        CompletableFuture<?> prepPizza = prepIngredients.thenApply(
                toppings -> {
                    System.out.println("    Restaurant> Spreading with tomato sauce and sprinkle with toppings: " + toppings);

                    return "Raw pizza with " + toppings;
                }
        );

        CompletableFuture<String> bakePizza = prepPizza.thenApply(
                rawPizza -> {
                    System.out.println("   Restaurant> Baking pizza: " + rawPizza);
                    try {
                        TimeUnit.MILLISECONDS.sleep(300);
                    } catch (InterruptedException e) {

                    }

                    return "Pizza";
                }
        );

        bakePizza.thenAccept(pizza -> System.out.println("Eating pizza " + pizza));

        System.out.println(sliceTomatoes.get());

        executorService.shutdown();
    }


}
