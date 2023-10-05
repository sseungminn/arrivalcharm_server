package com.hong.arrivalcharm.controller.destination;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hong.arrivalcharm.model.destination.Destination;
import com.hong.arrivalcharm.service.RestTemplateService;
import com.hong.arrivalcharm.service.destination.DestinationService;

import io.swagger.annotations.ApiOperation;

@RequestMapping("/api/v1/destination")
@RestController
public class DestinationController {

	@Autowired
	private DestinationService destinationService;
	
	@Autowired
	private RestTemplateService restTemplateService;
	
	// 내 경로 리스트
	@GetMapping("")
	@ApiOperation(value = "목적지 리스트 조회", notes = "")
	public @ResponseBody Map<String, Object> getDestinationList() throws Exception {
		Map<String, Object> result = null;
		try {
			result = destinationService.getDestinationList();
		} catch (Exception e) {
			throw e;
		}
		return result;
	}
	
	// 목적지 상세
	@GetMapping("/{id}")
	@ApiOperation(value = "목적지 상세 조회", notes = "")
	public @ResponseBody Map<String, Object> getDestination(@PathVariable int id) throws Exception {
		Map<String, Object> result = null;
		try {
			result = destinationService.getDestination(id);
			Destination destination = (Destination) result.get("destination");
			String lat = destination.getLat();
			String lon = destination.getLon();
			Map<String, String> weather = restTemplateService.getWeather(lat, lon);
		    result.put("weather", weather);
		} catch (Exception e) {
			throw e;
		}
		return result;
	}
	@PostMapping("")
	@ApiOperation(value = "목적지 추가", notes = "")
	public @ResponseBody Map<String, Object> createDestination(@RequestParam String name, @RequestParam String address, @RequestParam String lat, @RequestParam String lon) throws Exception {
		Map<String, Object> result = null;
		try {
			result = destinationService.createDestination(name, address, lat, lon);
		} catch (Exception e) {
			throw e;
		}
		return result; 
	}
	
	@PutMapping("/{id}")
	@ApiOperation(value = "목적지 수정", notes = "")
	public @ResponseBody Map<String, Object> modifyDestination(@PathVariable int id, @RequestParam String name, @RequestParam String lat, @RequestParam String lon) throws Exception {
		Map<String, Object> result = null;
		try {
			result = destinationService.modifyDestination(id, name, lat, lon);
		} catch (Exception e) {
			throw e;
		}
		return result; 
	}
	
	@DeleteMapping("/{id}")
	@ApiOperation(value = "목적지 삭제", notes = "")
	public @ResponseBody Map<String, Object> deleteDestination(@PathVariable int id){
		Map<String, Object> result = null;
		try {
			result = destinationService.deleteDestination(id);
		} catch (Exception e) {
			throw e;
		}
		return result; 
	}
}

