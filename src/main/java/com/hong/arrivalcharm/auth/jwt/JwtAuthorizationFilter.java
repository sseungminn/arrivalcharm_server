package com.hong.arrivalcharm.auth.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.hong.arrivalcharm.auth.PrincipalDetails;
import com.hong.arrivalcharm.define.ErrorCode;
import com.hong.arrivalcharm.model.auth.User;
import com.hong.arrivalcharm.repository.UserRepository;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
	@Autowired
	private UserRepository userRepository;
	
	public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository) {
		super(authenticationManager);
		this.userRepository = userRepository;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		// 토큰 검증 안하는 URL
		if(request.getRequestURI().startsWith("/") || request.getRequestURI().startsWith("/api/v1/auth/") || request.getRequestURI().startsWith("/api/v1/check/") || request.getRequestURI().startsWith("/swagger-") || request.getRequestURI().startsWith("/v2")) {
			chain.doFilter(request, response);
			return;
		}
		
		System.out.println("Authorization Request : " + request.getRequestURI());
		String jwtHeader = request.getHeader(JwtProperties.HEADER_STRING);
		System.out.println("jwtHeader : " + jwtHeader);
		// 테스트 파라미터 (실서버시 주석 해야함)
		if(!StringUtils.isEmpty(request.getParameter("testUserId"))) {
			int userId = Integer.parseInt(request.getParameter("testUserId"));
			User userEntity = userRepository.findById(userId);
			PrincipalDetails principalDetails = new PrincipalDetails(userEntity);
			Authentication authentication = new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());
			if(authentication != null) {
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}

			chain.doFilter(request, response);
			return;
		}

		// Confirm Header
		if(jwtHeader == null || !jwtHeader.startsWith(JwtProperties.TOKEN_PREFIX)) {
			response.sendError(ErrorCode.INVALID_TOKEN.getCode(), "INVALID TOKEN");
			return;
		}
		
		try {
			String jwtToken = request.getHeader(JwtProperties.HEADER_STRING).replace(JwtProperties.TOKEN_PREFIX, "");
			String id = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET)).build().verify(jwtToken).getClaim("id").toString();
			System.out.println("Session ID : " + id);

			if(id != null) {
				User userEntity = userRepository.findById(Integer.parseInt(id));
				PrincipalDetails principalDetails = new PrincipalDetails(userEntity);
				Authentication authentication = new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());
				
				// Authentication Save In Security Session forcely
				System.out.println("Success Making Session : " + authentication);
				if(authentication != null) {
					SecurityContextHolder.getContext().setAuthentication(authentication);
				}
			}

		chain.doFilter(request, response);
		
		} catch(TokenExpiredException e) {
			System.out.println("EXPIRED_TOKEN Exception");
		    response.sendError(ErrorCode.EXPIRED_TOKEN.getCode(), "EXPIRED TOKEN");
		} catch (JwtException e) {
            e.printStackTrace();
			System.out.println("INVALID_TOKEN Exception");
		    response.sendError(ErrorCode.INVALID_TOKEN.getCode(), "INVALID TOKEN");
        }
	}
}
