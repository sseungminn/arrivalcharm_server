package com.hong.arrivalcharm.model.auth;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.CreationTimestamp;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "User", indexes = {
		@Index(name = "idx_user_username", columnList = "username")
})
// 유저
public class User {
	@Id //primary Key
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Comment("유저 번호")
	private int id;	// int(11) / 유저 번호
	
	@Column(length = 100, nullable = false)
    @Comment("유저 이름")
	private String username; // VARCHAR(100) / 유저 이름 / nn
	
	@Column(length = 100, nullable = true)
    @Comment("유저 노출 이름")
	private String displayUsername;	// VARCHAR(100) / 유저 노출 이름 / null
	
	@Column(length = 255, nullable = false)
    @Comment("비밀번호")
	private String password;	// VARCHAR(255) / 비밀번호 / nn
	
	@Column(length = 100, nullable = true)
    @Comment("이메일(OAuth만 사용)")
	private String email;	// VARCHAR(100) / 이메일(Oauth만 사용) / null
	
	@Column(length = 20, nullable = false)
    @Comment("유저 권한")
	private String role;	// VARCHAR(20) / 유저 권한 (ROLE_USER, ROLE_ADMIN) / nn
	
	@Column(length = 20, nullable = true)
    @Comment("OAuth 종류")
	private String provider;	// VARCHAR(20) / Oauth 종류 /null
	
	@Column(length = 100, nullable = true)
    @Comment("OAuth ID")
	private String providerId;	// VARCHAR(100) / Oauth ID / null
	
	@Column(length = 255, nullable = true)
    @Comment("프로필 사진 경로")
	private String profilePath;	// VARCHAR(255) / 프로필 사진 경로 / null
	
	@Column(nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Comment("생성 시간")
	@CreationTimestamp
	private Timestamp createdAt; // Timestamp / 생성 시간 / nn / default: NOW()
	
	@Column(nullable = false, columnDefinition = "CHAR(1)")
	@ColumnDefault("'F'")
    @Comment("탈퇴 여부(T, F)")
	private String isResigned; // CHAR(1) / 탈퇴 여부(T, F) / nn / default: 'F'
	
	@Column(nullable = true, columnDefinition = "TIMESTAMP")
    @Comment("탈퇴 시간")
	private Timestamp resignedAt; // Timestamp / 탈퇴 시간 / null
	
	@Builder
	public User(String username, String password, String email, String role, String provider, String providerId, String profilePath, String displayUsername,
			Timestamp createdAt, String isResigned, Timestamp resignedAt) {
		this.username = username;
		this.password = password;
		this.email = email;
		this.role = role;
		this.provider = provider;
		this.providerId = providerId;
		this.profilePath = profilePath;
		this.displayUsername = displayUsername;
		this.createdAt = createdAt;
		this.isResigned = isResigned;
		this.resignedAt = resignedAt;
	}
}

