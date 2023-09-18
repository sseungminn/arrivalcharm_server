package com.hong.arrivalcharm.auth.jwt;

public interface JwtProperties {
	String SECRET = "ARRIVALCHARM_JTW_SECRET_KEY_@2seung";
	long EXPIRATION_TIME = 1000 * 60 * 30;
	long REFRESH_EXPIRATION_TIME = 1000 * 60 * 60 * 24 * 30;
	String TOKEN_PREFIX = "Bearer ";
	String HEADER_STRING = "Authorization";
}
