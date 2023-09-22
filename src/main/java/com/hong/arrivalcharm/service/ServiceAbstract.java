package com.hong.arrivalcharm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.hong.arrivalcharm.auth.jwt.JwtProperties;
import com.hong.arrivalcharm.lib.RequestUtils;
import com.hong.arrivalcharm.model.auth.User;
import com.hong.arrivalcharm.repository.UserRepository;

@Component
public class ServiceAbstract extends RequestUtils{
	
	@Autowired
	private UserRepository userRepository;
	
	protected User getUserSession() {
//		String jwtToken = this.getHttpServletRequest().getHeader(JwtProperties.HEADER_STRING).replace(JwtProperties.TOKEN_PREFIX, "");
//		int userId = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET)).build().verify(jwtToken).getClaim("id").asInt();
//		User user = userRepository.findById(userId);
		User user = userRepository.findById(1);
		return user;
	}
}
