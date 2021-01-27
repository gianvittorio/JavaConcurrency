package com.gianvittorio.concurrency.lesson3;

import java.util.concurrent.Flow;

public class IntegerSubscriber implements Flow.Subscriber<Integer>{
    private Flow.Subscription subscription;

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        this.subscription = subscription;

        System.out.format("%s: successfully subscribed.\n", Thread.currentThread().getName());

        subscription.request(1);
    }

    @Override
    public void onNext(Integer integer) {
        System.out.format("%s: successfully received %d.\n", Thread.currentThread().getName(), integer);

        subscription.request(1);
    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onComplete() {

    }
}
