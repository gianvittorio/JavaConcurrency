package com.gianvittorio.concurrency.lesson2;

import java.util.Arrays;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

public class ParallelStreams {
    public static void main(String[] args) {
        String[] words = {"gianvittorio", "I", "Vanessa", "Gigio", "mom", "musket"};

        ConcurrentMap<Character, Long> characterLongConcurrentMap = Arrays.stream(words)
                .parallel()
                .collect(Collectors.groupingByConcurrent(str -> str.charAt(0), Collectors.counting()));

        System.out.println(characterLongConcurrentMap);
    }
}
