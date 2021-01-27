package com.gianvittorio.concurrency.lesson3;

import java.util.concurrent.*;

public class WeatherForecastPublisher extends SubmissionPublisher<WeatherForecast> {
    private ScheduledExecutorService scheduler;

    private ScheduledFuture<?> periodicTask;

    public WeatherForecastPublisher() {
        super(Executors.newFixedThreadPool(2), Flow.defaultBufferSize());

        scheduler = Executors.newScheduledThreadPool(1);
        periodicTask = scheduler.scheduleAtFixedRate(() -> submit(WeatherForecast.nextRandomWeatherForecast()), 500, 500, TimeUnit.MILLISECONDS);
    }

    @Override
    public void close() {
        periodicTask.cancel(false);

        scheduler.shutdown();

        super.close();
    }

}
