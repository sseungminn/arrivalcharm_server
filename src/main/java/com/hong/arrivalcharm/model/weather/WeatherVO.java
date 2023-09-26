package com.hong.arrivalcharm.model.weather;

import java.util.List;

import lombok.Getter;

@Getter
public class WeatherVO {
    private double lat;
    private double lon;
    private String timezone;
    private int timezone_offset;
    private CurrentWeather current;
    private List<MinutelyData> minutely;

}