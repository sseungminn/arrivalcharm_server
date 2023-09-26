package com.hong.arrivalcharm.repository.destination;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hong.arrivalcharm.model.destination.RecentDestination;

public interface RecentDestinationRepository extends JpaRepository<RecentDestination, String>{

	@Query(value = "SELECT rd.* "
				 + "FROM RecentDestination rd "
				 + "INNER JOIN User u "
				 + "ON rd.userId = u.id "
				 + "WHERE rd.userId = :userId "
				 + "ORDER BY rd.createdAt DESC", nativeQuery = true)
	List<RecentDestination> myRecentSearchList(@Param("userId") int userId);
	
	Optional<RecentDestination> findById(int id);
	Optional<RecentDestination> findByIdAndUserId(int id, int userId);
}
