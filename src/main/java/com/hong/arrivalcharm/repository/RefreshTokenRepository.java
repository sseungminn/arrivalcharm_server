package com.hong.arrivalcharm.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hong.arrivalcharm.model.auth.RefreshToken;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {
    public RefreshToken findById(int id);
    public RefreshToken findByUserId(int userId);
}