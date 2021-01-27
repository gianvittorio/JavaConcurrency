package com.gianvittorio.concurrency.lesson3;

import java.util.Objects;
import java.util.Random;

public class MetricWeatherForecast {

    public static MetricWeatherForecast fromImperial(WeatherForecast imperial) {
        int windSpeedInKMH = (int) (imperial.getWindSpeedInMPH() * 1.60934),
                temperatureInC = (int) ((imperial.getTemperatureInF() - 32) / 1.8);

        return new MetricWeatherForecast(temperatureInC, windSpeedInKMH, imperial.getWeatherCondition());
    }

    private final int temperatureInC;
    private final int windSpeedInKMH;
    private final String weatherCondition;

    private static final Random random = new Random();
    private static final String[] allWeatherConditions = new String[]{"cloudy", "sunny", "rainy"};

    public MetricWeatherForecast(int temperatureInC, int windSpeedInKMH, String weatherCondition) {
        this.temperatureInC = temperatureInC;
        this.windSpeedInKMH = windSpeedInKMH;
        this.weatherCondition = weatherCondition;
    }

    @Override
    public String toString() {
        return String.format("%s %d F wind: %d mph", weatherCondition, temperatureInC, windSpeedInKMH);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MetricWeatherForecast that = (MetricWeatherForecast) o;
        return temperatureInC == that.temperatureInC && windSpeedInKMH == that.windSpeedInKMH && Objects.equals(weatherCondition, that.weatherCondition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(temperatureInC, windSpeedInKMH, weatherCondition);
    }
}
