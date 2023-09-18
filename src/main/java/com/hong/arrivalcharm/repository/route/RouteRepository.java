package com.hong.arrivalcharm.repository.route;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hong.arrivalcharm.model.route.Route;

public interface RouteRepository extends JpaRepository<Route, String>{

	@Query(value = "SELECT r.* "
				 + "FROM Route r "
				 + "INNER JOIN User u "
				 + "ON r.userId = u.id "
				 + "WHERE r.userId = :userId "
				 + "AND r.isDeleted = 'F'", nativeQuery = true)
	List<Route> myRoutes(@Param("userId") int userId);
	
	@Query(value = "UPDATE Route r "
				 + "SET r.isDeleted = 'T', "
				 + "r.deletedAt = :now "
				 + "WHERE r.id = :routeId")
	void deleteRoute(@Param("now") Timestamp now, @Param("routeId") int routeId);
}
