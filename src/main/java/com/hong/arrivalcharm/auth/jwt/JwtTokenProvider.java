package com.hong.arrivalcharm.auth.jwt;

import java.time.Duration;
import java.util.Date;

import javax.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.hong.arrivalcharm.auth.PrincipalDetails;
import com.hong.arrivalcharm.model.auth.RefreshToken;
import com.hong.arrivalcharm.repository.RefreshTokenRepository;

@Component
public class JwtTokenProvider {
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	private final long REFRESH_TIME = Duration.ofDays(30).toMillis(); 

    public enum JwtCode{
        DENIED,
        ACCESS,
        EXPIRED;
    }
    
	@Autowired
	private RefreshTokenRepository refreshTokenRepository;
	
	// token으로 사용자 속성정보 조회
	public Claim getClaimFromToken(String token, String key) {
		return JWT.require(Algorithm.HMAC512(JwtProperties.SECRET)).build().verify(token).getClaim(key);
	}

	// 토큰 만료일자 조회
//	public Date getExpirationDateFromToken(String token) {
//		return getClaimFromToken(token, Claims::getExpiration);
//	}
	
	// PrincipalDetails를 이용해 accessToken 생성
	public String generateAccessToken(PrincipalDetails principalDetails) {
		return createAccessToken(principalDetails);
	}
	
	// 엑세스 토큰 생성
    public String createAccessToken(PrincipalDetails principalDetails) {
    	return JWT.create()
    			.withSubject(principalDetails.getUsername())
    			.withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.EXPIRATION_TIME))
                .withIssuer(principalDetails.getUsername())
    			.withClaim("id", principalDetails.getUser().getId())
    			.withClaim("username", principalDetails.getUser().getUsername())
    			.withClaim("role", principalDetails.getUser().getRole())
    			.sign(Algorithm.HMAC512(JwtProperties.SECRET));
    }

    // 리프레시 토큰 생성
    public String createRefreshToken(int userId){
        return JWT.create()
                .withSubject(Integer.toString(userId))
                .withExpiresAt(new Date(System.currentTimeMillis() + REFRESH_TIME))
                .withIssuer(Integer.toString(userId))
    			.withClaim("userId", userId)
                .sign(Algorithm.HMAC512(JwtProperties.SECRET));
    }

	// JWT refreshToken 만료체크 후 재발급
	public RefreshToken reGenerateRefreshToken(int userId) throws Exception {
		log.info("[reGenerateRefreshToken] refreshToken 재발급 요청");

		RefreshToken refreshToken = refreshTokenRepository.findByUserId(userId);
		// refreshToken 정보가 존재하지 않는 경우
		if(refreshToken == null) {
			log.error("[reGenerateRefreshToken] refreshToken 정보가 존재하지 않습니다.");
			throw new Exception("[reGenerateRefreshToken] refreshToken 정보가 존재하지 않습니다.");
		}
		
		// refreshToken 만료 여부 체크
		try {
			int refreshUserId = JWT.decode(refreshToken.getToken()).getClaim("id").asInt();
			if (userId == refreshUserId) {
				JWT.require(Algorithm.HMAC512(JwtProperties.SECRET)).build().verify(refreshToken.getToken());

				return refreshToken;
			}
			
			log.error("[reGenerateRefreshToken] refreshToken이 만료되지 않았습니다.");
			throw new Exception("[reGenerateRefreshToken] refreshToken이 만료되지 않았습니다.");
		}
		// refreshToken이 만료된 경우 재발급
		catch(EntityNotFoundException | JWTVerificationException e) {
			refreshToken.setToken(createRefreshToken(userId));
			refreshTokenRepository.save(refreshToken);
			
			return refreshToken;
		}
		// 그 외 예외처리
		catch(Exception e) {
			log.error("[reGenerateRefreshToken] refreshToken 재발급 중 문제 발생 : {}", e.getMessage());
			throw new Exception("[reGenerateRefreshToken] refreshToken 재발급 중 문제 발생 : " + e.getMessage());
		}
	}
	
    // 토큰 검증
    public JwtCode validateToken(String token) {
        try {
            JWT.require(Algorithm.HMAC512(JwtProperties.SECRET)).build().verify(token);
            
            return JwtCode.ACCESS;
        } catch (JWTVerificationException e){
            // 만료된 경우에는 refresh token을 확인하기 위해
            return JwtCode.EXPIRED;
        } catch (JwtException | IllegalArgumentException e) {
            log.error("jwtException : {}", e);
        }
        return JwtCode.DENIED;
    }
}