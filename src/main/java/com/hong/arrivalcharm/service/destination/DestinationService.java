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
	public Map<String, Object> getDestinationList(){
		User user = this.getUserSession();
		int userId = user.getId();
		List<Destination> destinationList = destinationRepository.myDestinations(userId);
		List<Map<String, Object>> myDestinations = new ArrayList<Map<String, Object>>();
		for(Destination destination : destinationList) {
			Map<String, Object> map = new HashMap<>();
			map.put("id", destination.getId());
			map.put("name", destination.getName());
			map.put("address", destination.getAddress());
			map.put("lon", destination.getLat());
			map.put("lat", destination.getLon());
			map.put("userId", destination.getUser().getId());
			myDestinations.add(map);
		}
		Map<String, Object> result = new HashMap<>();
        result.put("destinationList", myDestinations);
        return result;
	}
	
	// 목적지 상세
	public Map<String, Object> getDestination(int id){
		User user = this.getUserSession();
		int userId = user.getId();
		Destination destination = destinationRepository.findByIdAndUserId(id, userId).get();
		Map<String, Object> result = new HashMap<>();
        result.put("destination", destination);
        return result;
	}
	
	// 내 경로 생성(추가)
	public Map<String, Object> createDestination(String name, String address, String lat, String lon){
		User user = this.getUserSession();
		int userId = user.getId();
		Destination destination = Destination.builder()
								.name(name)
								.address(address)
								.lat(lat)
								.lon(lon)
								.userId(userId)
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
		if(destination == null) {
			throw new NullPointerException("해당 ID("+id+")의 목적지가 존재하지 않습니다.");
		}
		destinationRepository.delete(destination);
		Map<String, Object> result = new HashMap<>();
		result.put("result", "success");
//        result.put("destination", destination);
        return result;
	}
	
	// 내 목적지 수정
	public Map<String, Object> modifyDestination(int id, String name, String lat, String lon) throws Exception{
		User user = this.getUserSession();
		int userId = user.getId();
		Destination destination = destinationRepository.findById(id).get();
		
		if(destination == null) {
			throw new NullPointerException("해당 ID("+id+")의 목적지가 존재하지 않습니다.");
		}
		
		if(userId != destination.getUser().getId()) {
			throw new Exception("본인이 작성한 목적지만 수정 가능합니다.");
		}
		destination.setName(name);
		destination.setLat(lat);
		destination.setLon(lon);
		Map<String, Object> result = new HashMap<>();
        result.put("destination", destination);
        return result;
	}
}
