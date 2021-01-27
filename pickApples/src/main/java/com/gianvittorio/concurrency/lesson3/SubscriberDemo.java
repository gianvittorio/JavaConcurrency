package com.gianvittorio.concurrency.lesson3;

import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class SubscriberDemo {
    public static void main(String[] args) {
        //Flow.Publisher<WeatherForecast> weatherForecastSubmissionPublisher = new OnDemandWeatherForecastPublisher();
//        SubmissionPublisher<WeatherForecast> weatherForecastSubmissionPublisher = new SubmissionPublisher<>();
        SubmissionPublisher<WeatherForecast> weatherForecastSubmissionPublisher = new WeatherForecastPublisher();

        weatherForecastSubmissionPublisher.subscribe(new DBSubscriber());
        weatherForecastSubmissionPublisher.subscribe(new TwitterSubscriber<WeatherForecast>());

        Flow.Processor<WeatherForecast, MetricWeatherForecast> metricProcessor = new UsToMetricProcessor();
        weatherForecastSubmissionPublisher.subscribe(metricProcessor);
        metricProcessor.subscribe(new TwitterSubscriber<MetricWeatherForecast>());


//        IntStream.iterate(0, i -> i + 1)
//                .limit(10000000)
//                .forEach(i -> weatherForecastSubmissionPublisher.submit(WeatherForecast.nextRandomWeatherForecast()));

        //weatherForecastSubmissionPublisher.close();
    }
}
