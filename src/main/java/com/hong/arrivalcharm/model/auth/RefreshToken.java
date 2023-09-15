package com.hong.arrivalcharm.model.auth;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import org.hibernate.annotations.Comment;
import org.hibernate.annotations.CreationTimestamp;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
@Table(name = "RefreshToken", indexes = {
		@Index(name = "idx_refreshtoken_userId", columnList = "userId")
})
// 리프레시 토큰
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("리프레시 토큰 번호")
    private int id; // int(11) / 리프레시 토큰 번호 / nn
    
    @Column(nullable = false)
    @Comment("유저 번호(F.K)")
    private int userId;	// INT(11) / 유저 번호(F.K) / nn
    
    @Column(nullable = false)
    @Comment("토큰")
    private String token;	// VARCHAR(255) / 토큰 / nn
    
    @Column(nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Comment("생성 시간")
	@CreationTimestamp
	private Timestamp createdAt; // Timestamp / 생성 시간 / nn / defualt: NOW()

    public void changeToken(String token) {
        this.token = token;
    }
    
	@Builder
	public RefreshToken(int userId, String token) {
		this.userId = userId;
		this.token = token;
	}
}
