package com.hong.arrivalcharm.controller.destination;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hong.arrivalcharm.service.destination.RecentDestinationService;

import io.swagger.annotations.ApiOperation;

@RequestMapping("/api/v1/destination/recent")
@RestController
public class RecentDestinationController {

	@Autowired
	private RecentDestinationService recentDestinationService;
	
	// 최근 검색 목록 조회
	@GetMapping("")
	@ApiOperation(value = "최근 검색 목적지 리스트 조회", notes = "")
	public @ResponseBody Map<String, Object> getRecentDestinationList() throws Exception {
		Map<String, Object> result = null;
		try {
			result = recentDestinationService.getRecentDestinationList();
		} catch (Exception e) {
			throw e;
		}
		return result;
	}
	
	@PostMapping("")
	@ApiOperation(value = "검색 목적지 추가", notes = "")
	public @ResponseBody Map<String, Object> createRecentDestination(@RequestParam String address, @RequestParam String lat, @RequestParam String lon) throws Exception {
		Map<String, Object> result = null;
		try {
			result = recentDestinationService.createRecentDestination(address, lat, lon);
		} catch (Exception e) {
			throw e;
		}
		return result; 
	}
	
	@PatchMapping("/{id}")
	@ApiOperation(value = "검색 목적지 재검색", notes = "")
	public @ResponseBody Map<String, Object> reuseRecentDestination(@PathVariable int id) throws Exception {
		Map<String, Object> result = null;
		try {
			result = recentDestinationService.reuseRecentDestination(id);
		} catch (Exception e) {
			throw e;
		}
		return result; 
	}
	
	@DeleteMapping("/{id}")
	@ApiOperation(value = "검색 목적지 삭제", notes = "")
	public @ResponseBody Map<String, Object> deleteRecentDestination(@PathVariable int id){
		Map<String, Object> result = null;
		try {
			result = recentDestinationService.deleteRecentDestination(id);
		} catch (Exception e) {
			throw e;
		}
		return result; 
	}
}

