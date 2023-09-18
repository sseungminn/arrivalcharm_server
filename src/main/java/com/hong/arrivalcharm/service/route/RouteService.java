package com.hong.arrivalcharm.service.route;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.hong.arrivalcharm.model.auth.User;
import com.hong.arrivalcharm.model.route.Route;
import com.hong.arrivalcharm.repository.route.RouteRepository;
import com.hong.arrivalcharm.service.ServiceAbstract;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RouteService extends ServiceAbstract {

	private final RouteRepository routeRepository;
	
	// 내 경로 리스트 조회
	public Map<String, Object> routeList(){
		User user = this.getUserSession();
		int userId = user.getId();
		List<Route> routeList = routeRepository.myRoutes(userId);
		List<Map<String, Object>> myRoutes = new ArrayList<Map<String, Object>>();
		for(Route route : routeList) {
			Map<String, Object> map = new HashMap<>();
			map.put("routeId", route.getId());
			map.put("routeName", route.getName());
			map.put("departures", route.getDepartures());
			map.put("arrivals", route.getArrivals());
			map.put("userId", route.getUser().getId());
			myRoutes.add(map);
		}
		Map<String, Object> result = new HashMap<>();
        result.put("routeList", myRoutes);
        return result;
	}
	
	// 내 경로 생성(추가)
	public Map<String, Object> createRoute(String name, String departures, String arrivals){
		User user = this.getUserSession();
		int userId = user.getId();
		Route route = Route.builder()
								.name(name)
								.userId(userId)
								.departures(departures)
								.arrivals(arrivals)
								.isDeleted("F")
								.build();
		routeRepository.save(route);
		Map<String, Object> result = new HashMap<>();
        result.put("route", route);
        return result;
	}
}
