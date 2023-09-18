package com.hong.arrivalcharm.config.oauth;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.hong.arrivalcharm.auth.PrincipalDetails;
import com.hong.arrivalcharm.config.oauth.provider.GoogleUserInfo;
import com.hong.arrivalcharm.config.oauth.provider.KakaoUserInfo;
import com.hong.arrivalcharm.config.oauth.provider.NaverUserInfo;
import com.hong.arrivalcharm.config.oauth.provider.OAuth2UserInfo;
import com.hong.arrivalcharm.model.auth.User;
import com.hong.arrivalcharm.repository.UserRepository;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private UserRepository userRepository;
	
	@SuppressWarnings("unchecked")
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {		
		OAuth2User oauth2User = super.loadUser(userRequest);
		OAuth2UserInfo oauth2UserInfo = null;
		
		if(userRequest.getClientRegistration().getRegistrationId().equals("google")) {
			oauth2UserInfo = new GoogleUserInfo(oauth2User.getAttributes());
		} else if(userRequest.getClientRegistration().getRegistrationId().equals("naver")) {
			oauth2UserInfo = new NaverUserInfo((Map<String, Object>)oauth2User.getAttributes().get("response"));
		} else if(userRequest.getClientRegistration().getRegistrationId().equals("kakao")) {
			oauth2UserInfo = new KakaoUserInfo(oauth2User.getAttributes());
		}
		
		String provider = oauth2UserInfo.getProvider();
		String providerId = oauth2UserInfo.getProviderId();
		String username = provider + "_" + providerId;
		String password = bCryptPasswordEncoder.encode("none");	// OAuth는 비밀번호를 사용하지 않으므로 아무값이나 입력
		String email = oauth2UserInfo.getEmail();
		String role = "ROLE_USER";
		
		User userEntity = userRepository.findByUsername(username);
		if(userEntity == null) {
			userEntity = User.builder()
					.username(username)
					.password(password)
					.email(email)
					.role(role)
					.provider(provider)
					.providerId(providerId)
					.build();
			userRepository.save(userEntity);
		}
		
		System.out.println("OAuth Login");
		
		return new PrincipalDetails(userEntity, oauth2User.getAttributes());
	}
}
