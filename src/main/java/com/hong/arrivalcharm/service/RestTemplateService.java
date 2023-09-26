package com.hong.arrivalcharm.service;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.hong.arrivalcharm.model.weather.WeatherVO;


@Service
public class RestTemplateService {

	@Value("${api.openweather}")
	private String weatherKey;
	
	public Map<String, String> getWeather(String lat, String lon){
        URI uri = UriComponentsBuilder
                .fromUriString("http://api.openweathermap.org") // Http 오류 -> https로 요청시 헤더에 토큰이 없어서 그런듯 -> http로 변경
                .path("/data/2.5/onecall")
                .queryParam("lat", lat)
                .queryParam("lon", lon)
                .queryParam("apiKey", weatherKey)
                .encode()
                .build()
                .toUri();
        RestTemplate restTemplate = new RestTemplate();
     // API 응답을 VO로 매핑하기 위해 ParameterizedTypeReference 사용
        ResponseEntity<WeatherVO> responseEntity = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<WeatherVO>() {}
        );
        WeatherVO weather = responseEntity.getBody();
        String temp = Math.round(weather.getCurrent().getTemp()-273.15) + "";
//        String icon = "https://openweathermap.org/img/w/"+weather.getCurrent().getWeather().get(0).getIcon()+".png";
        String icon = "http://localhost:8080/image/weather/"+weather.getCurrent().getWeather().get(0).getIcon()+".png";
        Map<String, String> result = new HashMap<>();
        result.put("temp", temp);
        result.put("icon", icon);
        return result;
    }
    
}
