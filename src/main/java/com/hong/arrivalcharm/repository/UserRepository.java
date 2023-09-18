package com.hong.arrivalcharm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.hong.arrivalcharm.model.auth.User;

public interface UserRepository extends JpaRepository<User, Integer>{
	public User findByUsername(String username);
	public User findById(int id);
	public User findByProviderAndProviderId(String provider, String providerId);
	
	@Modifying
	@Query(value = "UPDATE User u SET u.profilePath = :profilePath, u.displayUsername = :displayUsername WHERE u.id = :id")
	void updateUser(String profilePath, String displayUsername, int id);
}
