package com.hong.arrivalcharm.service.destination;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hong.arrivalcharm.define.Const;
import com.hong.arrivalcharm.model.auth.User;
import com.hong.arrivalcharm.model.destination.RecentDestination;
import com.hong.arrivalcharm.repository.destination.RecentDestinationRepository;
import com.hong.arrivalcharm.service.ServiceAbstract;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecentDestinationService extends ServiceAbstract {

	private final RecentDestinationRepository recentDestinationRepository;
	// 내 최근 검색 리스트 조회
	public Map<String, Object> getRecentDestinationList(){
		User user = this.getUserSession();
		int userId = user.getId();
		List<RecentDestination> myRecentSearchList = recentDestinationRepository.getMyRecentSearchList(userId);
		List<Map<String, Object>> myRecentDestinationList = new ArrayList<Map<String, Object>>();
		for(RecentDestination rd : myRecentSearchList) {
			Map<String, Object> map = new HashMap<>();
			map.put("id", rd.getId());
			map.put("address", rd.getAddress());
			map.put("lon", rd.getLat());
			map.put("lat", rd.getLon());
			map.put("searchedAt", rd.getSearchedAt().getTime());
			map.put("userId", rd.getUser().getId());
			myRecentDestinationList.add(map);
		}
		Map<String, Object> result = new HashMap<>();
        result.put("recentSearchList", myRecentDestinationList);
        return result;
	}
	
	// 내 경로 생성(추가)
	@Transactional
	public Map<String, Object> createRecentDestination(String address, String lat, String lon){
		User user = this.getUserSession();
		int userId = user.getId();
		long now = System.currentTimeMillis();
		int recentSearchCount = recentDestinationRepository.getMyRecentSearchList(userId).size();
		
		RecentDestination recentDestination = RecentDestination.builder()
								.address(address)
								.lat(lat)
								.lon(lon)
								.searchedAt(new Timestamp(now))
								.userId(userId)
								.build();
		recentDestinationRepository.save(recentDestination);
		
		// 최근 검색 limit 넘으면
		if(recentSearchCount >= Const.RECENT_SEARCH_LIMIT) {
			// 20개 제외하고 삭제
			recentDestinationRepository.deleteAllExceptLatest(Const.RECENT_SEARCH_LIMIT);
		}
		
		Map<String, Object> result = new HashMap<>();
		Map<String, Object> map = new HashMap<>();
		map.put("id", recentDestination.getId());
		map.put("address", address);
		map.put("lon", lat);
		map.put("lat", lon);
		map.put("searchedAt", now);
		map.put("userId", userId);
        result.put("recentDestination", map);
        return result;
	}
	
	// 내 경로 재사용
	@Transactional
	public Map<String, Object> reuseRecentDestination(int id){
		User user = this.getUserSession();
		int userId = user.getId();
		long now = System.currentTimeMillis();
		
		RecentDestination recentDestination = recentDestinationRepository.findById(id).get();
		if(recentDestination == null) {
			throw new NullPointerException("해당 id(" + id + ")의 목적지가 없습니다.");
		}
		recentDestination.setSearchedAt(new Timestamp(now));
		
		Map<String, Object> result = new HashMap<>();
		Map<String, Object> map = new HashMap<>();
		map.put("id", recentDestination.getId());
		map.put("address", recentDestination.getAddress());
		map.put("lon", recentDestination.getLon());
		map.put("lat", recentDestination.getLat());
		map.put("searchedAt", now);
		map.put("userId", userId);
        result.put("recentDestination", map);
        return result;
	}
		
	// 목적지 삭제
	@Transactional
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
	
	// 최근 목적지 전체 삭제
	@Transactional
	public Map<String, Object> deleteAllRecentDestination(){
		User user = this.getUserSession();
		int userId = user.getId();
		List<RecentDestination> recentDestinationList = recentDestinationRepository.getMyRecentSearchList(userId);
		if(recentDestinationList == null || recentDestinationList.isEmpty()) {
			throw new NullPointerException("최근 검색한 목적지가 없습니다.");
		}
		for(RecentDestination rd : recentDestinationList) {
			recentDestinationRepository.delete(rd);
		}
		Map<String, Object> result = new HashMap<>();
		result.put("result", "success");
//	        result.put("destination", destination);
        return result;
	}	
}
