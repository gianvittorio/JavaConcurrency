package com.gianvittorio.concurrency.lesson3;

import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;

public class UsToMetricProcessor extends SubmissionPublisher<MetricWeatherForecast>
        implements Flow.Processor<WeatherForecast, MetricWeatherForecast> {
    private Flow.Subscription subscription;

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        this.subscription = subscription;

        subscription.request(1);
    }

    @Override
    public void onNext(WeatherForecast weatherForecast) {
        submit(MetricWeatherForecast.fromImperial(weatherForecast));

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
