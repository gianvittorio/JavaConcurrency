package com.gianvittorio.pickApples;

import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class AppleTree {
    private final String treelabel;
    private final int numberOfApples;

    public static AppleTree[] newTreeGarden(int size) {
        return IntStream.range(0, size)
                .mapToObj(i -> new AppleTree("Tree#" + i))
                .toArray(AppleTree[]::new);
    }

    public AppleTree(String treelabel) {
        this.treelabel = treelabel;
        this.numberOfApples = 3;
    }

    public int pickApples() {
        return pickApples("");
    }

    public int pickApples(String workerName) {
        try {
            System.out.printf("%s started picking apples from %s \n", workerName, treelabel);

            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return numberOfApples;
    }
}
