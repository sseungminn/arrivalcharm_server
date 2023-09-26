package com.hong.arrivalcharm.model.weather;

import java.util.List;

import lombok.Getter;

@Getter
public class CurrentWeather {
    private long dt;
    private long sunrise;
    private long sunset;
    private double temp;
    private double feels_like;
    private int pressure;
    private int humidity;
    private double dew_point;
    private int uvi;
    private int clouds;
    private int visibility;
    private double wind_speed;
    private int wind_deg;
    private double wind_gust;
    private List<WeatherDescription> weather;

}
