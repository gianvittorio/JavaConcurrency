package com.gianvittorio.concurrency.lesson3;

import java.util.concurrent.Flow;
import java.util.concurrent.TimeUnit;

public class TwitterSubscriber<T> implements Flow.Subscriber<T> {
    private final String name = "Twitter Subscriber";

    private Flow.Subscription subscription;

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        System.out.println(name + " subscribed!");

        this.subscription = subscription;
        subscription.request(1);
    }

    @Override
    public void onNext(T weatherForecast) {
        System.out.println(Thread.currentThread().getName() + " Twitting: " + weatherForecast);

        subscription.request(1);
    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onComplete() {

    }
}
