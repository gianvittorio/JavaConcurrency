package com.gianvittorio.concurrency.lesson3;

import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;
import java.util.concurrent.atomic.AtomicInteger;

public class IntegerSumProcessor extends SubmissionPublisher<Integer> implements Flow.Processor<Integer, Integer> {
    private Flow.Subscription subscription;

    private AtomicInteger sum = new AtomicInteger(0);

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        System.out.format("%s: successfully subscribed.\n", Thread.currentThread().getName());

        this.subscription = subscription;

        subscription.request(1);
    }

    @Override
    public void onNext(Integer integer) {
        System.out.format("%s: successfully consumed %d, transforming it into %d\n", Thread.currentThread().getName(), integer, sum.addAndGet(integer));

        submit(sum.get());

        subscription.request(1);
    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onComplete() {
        close();
    }
}
