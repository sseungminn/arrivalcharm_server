package com.hong.arrivalcharm.controller.auth;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hong.arrivalcharm.service.auth.AuthService;

import io.swagger.annotations.ApiOperation;


@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
	@Autowired
	private AuthService authService;
//	@Autowired
//	private OAuth2LoginService oAuth2LoginService;

	// JWT accessToken 재발급
	@ApiOperation(value = "JWT accessToken 재발급", notes = "")
	@PostMapping("/accessToken")
	public @ResponseBody Map<String, Object> refreshToken(@RequestBody String refreshToken) throws Exception {
		System.out.println("Controller /Params: " + refreshToken.replaceAll("\"", ""));
		Map<String, Object> result = null;
		
		try {
			result = authService.refreshToken(refreshToken);
		} catch (Exception e) {
			throw e;
		}
		
		return result;
	}
	
	// OAuth 로그인
	@ApiOperation(value = "로그인 요청", notes = "")
	@PostMapping("/login/oauth")
	public @ResponseBody Map<String, Object> oauth(@RequestBody Map<String, String> oAuth2UserInfo) throws Exception {		
		Map<String, Object> result = null;
		
		try {
			result = authService.oAuth2Login(oAuth2UserInfo);
			System.out.println("Controller / oauth: " + result);
		} catch (Exception e) {
			throw e;
		}
		
		return result;
		
	}
}
