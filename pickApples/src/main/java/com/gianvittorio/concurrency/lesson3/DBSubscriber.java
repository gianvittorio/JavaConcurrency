package com.gianvittorio.concurrency.lesson3;

import java.util.concurrent.Flow;

public class DBSubscriber implements Flow.Subscriber<WeatherForecast> {
    private static final String NAME = "DB Subscriber";

    private Flow.Subscription subscription;

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        this.subscription = subscription;

        System.out.println(NAME + " subscribed!");

        subscription.request(1);
    }

    @Override
    public void onNext(WeatherForecast weatherForecast) {
        System.out.println(Thread.currentThread().getName() + " Saving to DB:" + weatherForecast);
        subscription.request(1);
    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onComplete() {

    }
}
