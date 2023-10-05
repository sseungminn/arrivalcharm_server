package com.hong.arrivalcharm.service.auth;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hong.arrivalcharm.auth.PrincipalDetails;
import com.hong.arrivalcharm.auth.jwt.JwtTokenProvider;
import com.hong.arrivalcharm.model.auth.RefreshToken;
import com.hong.arrivalcharm.model.auth.User;
import com.hong.arrivalcharm.repository.RefreshTokenRepository;
import com.hong.arrivalcharm.repository.UserRepository;
import com.hong.arrivalcharm.service.ServiceAbstract;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
//@Slf4j
public class AuthService extends ServiceAbstract {
    private final UserRepository userRepository;
//    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
//    private final PrincipalDetailsService principalDetailsService;
	private final RefreshTokenRepository refreshTokenRepository;
//	private final PrincipalOauth2UserService principalOauth2UserService;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	
    @Transactional()
    public Map<String, Object> refreshToken(String refreshToken) throws Exception {
    	// refreshToken 정보 조회
		int userId = jwtTokenProvider.getClaimFromToken(refreshToken.replaceAll("\"", ""), "userId").asInt();
		RefreshToken token = jwtTokenProvider.reGenerateRefreshToken(userId);
		
		System.out.println("Controller /refreshToken: " + token);
		System.out.println("Controller /userId: " + userId);
		
		String userRefreshToken = token.getToken();
		
        Map<String, Object> result = new HashMap<>();
        
		// accessToken 재발급
		User userEntity = userRepository.findById(userId);
		PrincipalDetails principalDetails = new PrincipalDetails(userEntity);
		String newAccessToken = jwtTokenProvider.generateAccessToken(principalDetails);
		result.put("accessToken", newAccessToken);
		result.put("refreshToken", userRefreshToken);
        return result;
    }
    
	@Transactional()
    public Map<String, Object> oAuth2Login(Map<String, String> oAuth2UserInfo) throws Exception {
		System.out.println("Service Oauth2 : " + oAuth2UserInfo);
		
		String provider = oAuth2UserInfo.get("provider");
		String providerId = oAuth2UserInfo.get("id");
		String profilePath = "/image/default/profile.jpg";
		String username = oAuth2UserInfo.get("name");
		String password = bCryptPasswordEncoder.encode(oAuth2UserInfo.get("password"));
		String email = oAuth2UserInfo.get("email");
		String role = "ROLE_USER";
		
		User userEntity = userRepository.findByProviderAndProviderId(provider, providerId);
		if(userEntity == null) {
			userEntity = User.builder()
					.username(username)
					.displayUsername(username)
					.password(password)
					.email(email)
					.role(role)
					.provider(provider)
					.providerId(providerId)
					.profilePath(profilePath)
					.build();
			userRepository.save(userEntity);
		}
		
		PrincipalDetails principalDetails = new PrincipalDetails(userEntity);
		String accessToken = "";
		String refreshToken = "";
		
		int userId = userEntity.getId();
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
		
        Map<String, Object> tokens = new HashMap<>();
        tokens.put("status", "success");
        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);
        tokens.put("userId", Integer.toString(userEntity.getId()));
        tokens.put("name", userEntity.getDisplayUsername());
        tokens.put("photo", userEntity.getProfilePath());
		
        return tokens;
    }
}