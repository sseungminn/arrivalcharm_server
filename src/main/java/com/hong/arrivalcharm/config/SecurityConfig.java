package com.hong.arrivalcharm.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.filter.CorsFilter;

import com.hong.arrivalcharm.auth.jwt.JwtAuthenticationFilter;
import com.hong.arrivalcharm.auth.jwt.JwtAuthorizationFilter;
import com.hong.arrivalcharm.auth.jwt.JwtTokenProvider;
import com.hong.arrivalcharm.config.oauth.PrincipalOauth2UserService;
import com.hong.arrivalcharm.repository.RefreshTokenRepository;
import com.hong.arrivalcharm.repository.UserRepository;

import lombok.RequiredArgsConstructor;


@SuppressWarnings("deprecation")
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private PrincipalOauth2UserService pricipalOauthUserService;

	private final CorsFilter corsFilter;
	private final JwtTokenProvider jwtTokenProvider;
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RefreshTokenRepository refreshTokenRepository;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		.and()
		.addFilter(corsFilter)
		.formLogin().disable()
		.httpBasic().disable()
		.addFilter(new JwtAuthenticationFilter(authenticationManager(), jwtTokenProvider, refreshTokenRepository))
		.addFilter(new JwtAuthorizationFilter(authenticationManager(), userRepository))
		.authorizeRequests()
		.antMatchers("/**","/api/v1/auth/**", "/api/v1/check/**", "/swagger-ui/**", "/swagger**", "/v2**").permitAll()
		.antMatchers("/api/v1/**").authenticated()
		.antMatchers("/api/v1/admin/**").access("hasRole('ROLE_ADMIN')")
//		.antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
		.anyRequest().permitAll()
		.and()
		.oauth2Login()
		.loginPage("/login")
		.defaultSuccessUrl("/")
		.userInfoEndpoint()
		.userService(pricipalOauthUserService);
	}
}
