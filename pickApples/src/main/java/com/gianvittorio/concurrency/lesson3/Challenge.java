package com.gianvittorio.concurrency.lesson3;

public class Challenge {
    public static void main(String[] args) {
        IntegerPublisher integerPublisher = new IntegerPublisher();
        integerPublisher.subscribe(new IntegerSubscriber());

        IntegerSumProcessor processor = new IntegerSumProcessor();
        integerPublisher.subscribe(processor);
        processor.subscribe(new IntegerSubscriber());
    }
}
