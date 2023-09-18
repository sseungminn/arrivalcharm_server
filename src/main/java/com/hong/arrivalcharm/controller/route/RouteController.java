package com.hong.arrivalcharm.controller.route;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hong.arrivalcharm.service.route.RouteService;

@RequestMapping("/api/v1/route")
@RestController
public class RouteController {

	@Autowired
	private RouteService routeService;
	
	// 내 경로 리스트
	@GetMapping("")
	public @ResponseBody Map<String, Object> routes() throws Exception {
		Map<String, Object> result = null;
		try {
			result = routeService.routeList();
		} catch (Exception e) {
			throw e;
		}
		return result;
	}
	
	@PostMapping("")
	public @ResponseBody Map<String, Object> createRoute(@RequestParam String name, @RequestParam String departures, @RequestParam String arrivals) throws Exception {
		Map<String, Object> result = null;
		try {
			result = routeService.createRoute(name, departures, arrivals);
		} catch (Exception e) {
			throw e;
		}
		return result; 
	}
}
