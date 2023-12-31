package com.hong.arrivalcharm.model.destination;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;

import com.hong.arrivalcharm.model.auth.User;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
@ToString
@DynamicInsert
public class RecentDestination {
	@Id //primary Key
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("최근 검색한 목적지 ID")
	private int id; // INT(11) / 최근 검색 목적지 id / nn
	
	@Column(length = 100, nullable = false)
    @Comment("검색 목적지 주소")
	private String address; // VARCHAR(100) / 검색 목적지 주소 / nn
	
	@Column(length = 20, nullable = false)
    @Comment("검색 목적지 위도")
	private String lat; // VARCHAR(100) / 검색 목적지 위도 / nn
	
	@Column(length = 20, nullable = false)
    @Comment("검색 목적지 경도")
	private String lon; // VARCHAR(100) / 검색 목적지 경도 / nn
	
	@Column(nullable = false)
    @Comment("유저 번호(F.K)")
	private int userId;	// INT(11) / 유저 번호(F.K) / nn
	
	@Column(nullable = false, columnDefinition = "TIMESTAMP")
    @Comment("검색 시간")
	private Timestamp searchedAt; // Timestamp / 검색 시간 / nn / defualt: NOW()
	
	@OneToOne
    @JoinColumn(name = "userId", insertable = false, updatable = false)
    private User user;

	@Builder
	public RecentDestination(String address, String lat, String lon, Timestamp searchedAt, int userId) {
		this.address = address;
		this.lat = lat;
		this.lon = lon;
		this.searchedAt = searchedAt;
		this.userId = userId;
	}
}
