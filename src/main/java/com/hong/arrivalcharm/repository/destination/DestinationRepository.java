package com.hong.arrivalcharm.repository.destination;

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
	
	Optional<Destination> findById(int id);
	Optional<Destination> findByIdAndUserId(int id, int userId);
}
