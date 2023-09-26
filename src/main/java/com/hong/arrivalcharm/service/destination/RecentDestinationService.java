package com.hong.arrivalcharm.service.destination;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.hong.arrivalcharm.model.auth.User;
import com.hong.arrivalcharm.model.destination.RecentDestination;
import com.hong.arrivalcharm.repository.destination.RecentDestinationRepository;
import com.hong.arrivalcharm.service.ServiceAbstract;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecentDestinationService extends ServiceAbstract {

	private final RecentDestinationRepository recentDestinationRepository;
	
	// 내 경로 리스트 조회
	public Map<String, Object> destinationList(){
		User user = this.getUserSession();
		int userId = user.getId();
		List<RecentDestination> myRecentSearchList = recentDestinationRepository.myRecentSearchList(userId);
		List<Map<String, Object>> myRecentDestinationList = new ArrayList<Map<String, Object>>();
		for(RecentDestination rd : myRecentSearchList) {
			Map<String, Object> map = new HashMap<>();
			map.put("id", rd.getId());
			map.put("lon", rd.getLat());
			map.put("lat", rd.getLon());
			map.put("userId", rd.getUser().getId());
			myRecentDestinationList.add(map);
		}
		Map<String, Object> result = new HashMap<>();
        result.put("recentSearchList", myRecentDestinationList);
        return result;
	}
	
	// 내 경로 생성(추가)
	public Map<String, Object> createRecentDestination(String lat, String lon){
		User user = this.getUserSession();
		int userId = user.getId();
		RecentDestination recentDestination = RecentDestination.builder()
								.lat(lat)
								.lon(lon)
								.userId(userId)
								.build();
		recentDestinationRepository.save(recentDestination);
		Map<String, Object> result = new HashMap<>();
        result.put("recentDestination", recentDestination);
        return result;
	}
	
	// 목적지 삭제
	public Map<String, Object> deleteRecentDestination(int id){
		User user = this.getUserSession();
		int userId = user.getId();
		RecentDestination recentDestination = recentDestinationRepository.findByIdAndUserId(id, userId).get();
		if(recentDestination == null) {
			throw new NullPointerException("해당 ID("+id+")의 목적지가 존재하지 않습니다.");
		}
		recentDestinationRepository.delete(recentDestination);
		Map<String, Object> result = new HashMap<>();
		result.put("result", "success");
//        result.put("destination", destination);
        return result;
	}
	
}
