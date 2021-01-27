package com.gianvittorio.concurrency.lesson3;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Flow;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

public class OnDemandWeatherForecastPublisher implements Flow.Publisher<WeatherForecast> {
    private final ExecutorService executorService = ForkJoinPool.commonPool();

    @Override
    public void subscribe(Flow.Subscriber<? super WeatherForecast> subscriber) {
        subscriber.onSubscribe(new OnDemandForecastSubscription(subscriber, executorService));
    }

    private class OnDemandForecastSubscription implements Flow.Subscription {
        private final Flow.Subscriber<? super WeatherForecast> subscriber;
        private final ExecutorService executorService;
        private Future<?> future;

        public OnDemandForecastSubscription(Flow.Subscriber<? super WeatherForecast> subscriber, ExecutorService executorService) {
            this.subscriber = subscriber;
            this.executorService = executorService;
        }

        @Override
        public synchronized void request(long l) {
            if (l < 0) {
                IllegalArgumentException ex = new IllegalArgumentException();
                executorService.execute(() -> subscriber.onError(ex));

                return;
            }

            LongStream.range(0, l)
                    .forEach(i -> {
                        future = executorService.submit(() -> {
                            subscriber.onNext(WeatherForecast.nextRandomWeatherForecast());
                        });
                    });
        }

        @Override
        public synchronized void cancel() {

        }
    }
}
