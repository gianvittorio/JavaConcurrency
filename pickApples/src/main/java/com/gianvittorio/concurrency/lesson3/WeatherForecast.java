package com.gianvittorio.concurrency.lesson3;

import java.util.Objects;
import java.util.Random;

public class WeatherForecast {
    private final int temperatureInF,
            windSpeedInMPH;
    private final String weatherCondition;

    private static final Random random = new Random();
    private static final String[] allWeatherConditions = new String[]{"cloudy", "sunny", "rainy"};

    public int getTemperatureInF() {
        return temperatureInF;
    }

    public int getWindSpeedInMPH() {
        return windSpeedInMPH;
    }

    public String getWeatherCondition() {
        return weatherCondition;
    }

    public static WeatherForecast nextRandomWeatherForecast() {
        int pos = random.nextInt(allWeatherConditions.length);
        String weatherCondition = allWeatherConditions[pos];

        int temperatureInF = random.nextInt(95),
                windSpeedINMPH = 5 + random.nextInt(30);

        return new WeatherForecast(temperatureInF, windSpeedINMPH, weatherCondition);
    }

    public WeatherForecast(int temperatureInF, int windSpeedInMPH, String weatherCondition) {
        super();
        this.temperatureInF = temperatureInF;
        this.windSpeedInMPH = windSpeedInMPH;
        this.weatherCondition = weatherCondition;
    }

    @Override
    public String toString() {
        return String.format("%s %d F wind: %d mph", weatherCondition, temperatureInF, windSpeedInMPH);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WeatherForecast that = (WeatherForecast) o;
        return temperatureInF == that.temperatureInF && windSpeedInMPH == that.windSpeedInMPH && Objects.equals(weatherCondition, that.weatherCondition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(temperatureInF, windSpeedInMPH, weatherCondition);
    }
}
