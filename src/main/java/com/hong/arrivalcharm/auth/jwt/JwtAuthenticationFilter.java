package com.hong.arrivalcharm.auth.jwt;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hong.arrivalcharm.auth.PrincipalDetails;
import com.hong.arrivalcharm.model.auth.RefreshToken;
import com.hong.arrivalcharm.model.auth.User;
import com.hong.arrivalcharm.repository.RefreshTokenRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	private final AuthenticationManager authenticationManager;
	private final JwtTokenProvider jwtTokenProvider;
	private final RefreshTokenRepository refreshTokenRepository;
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
		System.out.println("Trying Login..");
		
		try {
			ObjectMapper om = new ObjectMapper();
			User user = om.readValue(request.getInputStream(), User.class);
			
			// 사용자의 권한 정보 가져오기
			List<GrantedAuthority> authorities = new ArrayList<>();
			authorities.add(new SimpleGrantedAuthority(user.getRole().toString()));
			
			UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), authorities);
			Authentication authentication = authenticationManager.authenticate(authenticationToken);
			
			return authentication;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		System.out.println("Success Authentication");
		
		PrincipalDetails principalDetails = (PrincipalDetails)authResult.getPrincipal();
		
		String accessToken = "";
		String refreshToken = "";
		try {
			int userId = principalDetails.getUser().getId();
			accessToken = jwtTokenProvider.createAccessToken(principalDetails);
			refreshToken = jwtTokenProvider.createRefreshToken(userId);
			
			// 리프레시토큰 넣기
			RefreshToken token = refreshTokenRepository.findByUserId(userId);
			if(token == null) {
				token = RefreshToken.builder()
					.userId(userId)
					.token(refreshToken)
					.build();
			} else {
				token.setToken(refreshToken);
			}

			refreshTokenRepository.save(token);
		} catch (Exception e) {
			e.printStackTrace();
		}
        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);
		
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), tokens);
	}
}
