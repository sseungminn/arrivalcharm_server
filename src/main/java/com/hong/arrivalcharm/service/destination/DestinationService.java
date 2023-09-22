package com.hong.arrivalcharm.service.destination;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.hong.arrivalcharm.model.auth.User;
import com.hong.arrivalcharm.model.destination.Destination;
import com.hong.arrivalcharm.repository.destination.DestinationRepository;
import com.hong.arrivalcharm.service.ServiceAbstract;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DestinationService extends ServiceAbstract {

	private final DestinationRepository destinationRepository;
	
	// 내 경로 리스트 조회
	public Map<String, Object> destinationList(){
		User user = this.getUserSession();
		int userId = user.getId();
		List<Destination> routeList = destinationRepository.myDestinations(userId);
		List<Map<String, Object>> myRoutes = new ArrayList<Map<String, Object>>();
		for(Destination destination : routeList) {
			Map<String, Object> map = new HashMap<>();
			map.put("id", destination.getId());
			map.put("name", destination.getName());
			map.put("lon", destination.getLat());
			map.put("lat", destination.getLon());
			map.put("userId", destination.getUser().getId());
			myRoutes.add(map);
		}
		Map<String, Object> result = new HashMap<>();
        result.put("routeList", myRoutes);
        return result;
	}
	
	// 내 경로 생성(추가)
	public Map<String, Object> createDestination(String name, String lat, String lon){
		User user = this.getUserSession();
		int userId = user.getId();
		Destination destination = Destination.builder()
								.name(name)
								.lat(lat)
								.lon(lon)
								.userId(userId)
								.isDeleted("F")
								.build();
		destinationRepository.save(destination);
		Map<String, Object> result = new HashMap<>();
        result.put("destination", destination);
        return result;
	}
	
	// 목적지 삭제
	public Map<String, Object> deleteDestination(int id){
		User user = this.getUserSession();
		int userId = user.getId();
		Destination destination = destinationRepository.findByIdAndUserId(id, userId).get();
		destinationRepository.delete(destination);
		Map<String, Object> result = new HashMap<>();
		result.put("result", "success");
//        result.put("destination", destination);
        return result;
	}
}
