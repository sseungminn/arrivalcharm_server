package com.hong.arrivalcharm.repository.destination;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hong.arrivalcharm.model.destination.RecentDestination;

public interface RecentDestinationRepository extends JpaRepository<RecentDestination, String>{

	@Query(value = "SELECT rd "
				 + "FROM RecentDestination rd "
				 + "JOIN FETCH rd.user u "
				 + "WHERE u.id = :userId "
				 + "ORDER BY rd.searchedAt DESC")
	List<RecentDestination> getMyRecentSearchList(@Param("userId") int userId);
	
	Optional<RecentDestination> findById(int id);
	Optional<RecentDestination> findByIdAndUserId(int id, int userId);
	
	@Modifying
	@Query(value = "DELETE FROM recentdestination "
				 + "WHERE recentdestination.id NOT IN ( "
				 + "    SELECT id "
				 + "    FROM ( "
				 + "        SELECT recentdestination.id "
				 + "        FROM recentdestination "
				 + "        INNER JOIN user ON user.id = recentdestination.userId "
				 + "        ORDER BY recentdestination.searchedAt DESC "
				 + "        LIMIT :limit "
				 + "    ) subquery "
				 + ");", nativeQuery = true)
	void deleteAllExceptLatest(@Param("limit") int limit);
}
