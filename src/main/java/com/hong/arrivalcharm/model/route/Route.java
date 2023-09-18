package com.hong.arrivalcharm.model.route;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.UpdateTimestamp;

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
// 그룹톡
public class Route {
	@Id //primary Key
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("경로 번호")
	private int id; // INT(11) / 경로 번호 / nn
	
	@Column(length = 100, nullable = false)
    @Comment("경로 이름")
	private String name; // VARCHAR(100) / 경로 이름 / nn
	
	@Column(length = 100, nullable = false)
    @Comment("출발지")
	private String departures; // VARCHAR(100) / 출발지 / nn
	
	@Column(length = 100, nullable = false)
    @Comment("목적지")
	private String arrivals; // VARCHAR(100) / 목적지 / nn
	
	@Column(nullable = false)
    @Comment("유저 번호(F.K)")
	private int userId;	// INT(11) / 유저 번호(F.K) / nn
	
	@Column(nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    @Comment("수정 시간")
	@UpdateTimestamp
	private Timestamp updatedAt; // Timestamp / 수정 시간 / nn
	
	@Column(updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Comment("생성 시간")
	@CreationTimestamp
	private Timestamp createdAt; // Timestamp / 생성 시간 / nn / defualt: NOW()
	
	@Column(nullable = false, columnDefinition = "CHAR(1)")
	@ColumnDefault("'F'")
    @Comment("삭제 여부(T, F)")
	private String isDeleted; // CHAR(1) / 삭제 여부(T, F) / nn / default : F
	
	@Column(nullable = true, columnDefinition = "TIMESTAMP")
    @Comment("삭제 시간")
	private Timestamp deletedAt; // Timestamp / 삭제 시간 / null
	
	@OneToOne
    @JoinColumn(name = "userId", insertable = false, updatable = false)
    private User user;

	@Builder
	public Route(String name, String departures, String arrivals, int userId, String isDeleted) {
		this.name = name;
		this.departures = departures;
		this.arrivals = arrivals;
		this.userId = userId;
		this.isDeleted = isDeleted;
	}
}
