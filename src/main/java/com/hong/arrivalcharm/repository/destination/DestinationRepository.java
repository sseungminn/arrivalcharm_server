package com.hong.arrivalcharm.repository.destination;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hong.arrivalcharm.model.destination.Destination;

public interface DestinationRepository extends JpaRepository<Destination, String>{

	@Query(value = "SELECT d.* "
				 + "FROM Destination d "
				 + "INNER JOIN User u "
				 + "ON d.userId = u.id "
				 + "WHERE d.userId = :userId "
				 + "AND d.isDeleted = 'F'", nativeQuery = true)
	List<Destination> myDestinations(@Param("userId") int userId);
	
	@Query(value = "UPDATE Destination d "
				 + "SET d.isDeleted = 'T', "
				 + "d.deletedAt = :now "
				 + "WHERE d.id = :routeId")
	void deleteDestination(@Param("now") Timestamp now, @Param("routeId") int routeId);
	
	Optional<Destination> findByIdAndUserId(int id, int userId);
}
