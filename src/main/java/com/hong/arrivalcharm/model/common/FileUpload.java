package com.hong.arrivalcharm.model.common;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Entity
@Data
@DynamicInsert
// 파일 업로드
public class FileUpload {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("파일 업로드 번호")
    private int id; // INT(11) / 파일 업로드 번호 / nn
    
    @Column(nullable = false)
    @Comment("원본 이름")
    private String originName; // VARCHAR(255) / 원본 이름 / nn
    
    @Column(length = 50, nullable = false)
    @Comment("서버 저장 이름")
    private String saveName; // VARCHAR(50) / 서버 저장 이름 / nn
    
    @Column(nullable = false)
    @Comment("서버 저장 경로")
    private String path; // VARCHAR(255) / 서버 저장 경로 / nn
    
    @Column(nullable = false)
    @Comment("파일 사이즈")
    @ColumnDefault("0")
    private int size; // INT(11) / 파일 사이즈 / nn / default: 0
    
    @Column(nullable = false, columnDefinition = "SMALLINT")
    @Comment("파일 넓이")
    @ColumnDefault("0")
    private int width; // SMALLINT / 파일 넓이 / nn / default: 0
    
    @Column(nullable = false, columnDefinition = "SMALLINT")
    @Comment("파일 높이")
    @ColumnDefault("0")
    private int height; // SMALLINT / 파일 높이 / nn / default: 0
    
    @Column(nullable = false, columnDefinition = "CHAR(1)")
	@ColumnDefault("'F'")
    @Comment("삭제 여부(T, F)")
    private String isDeleted; //  CHAR(1) / 삭제 여부(T, F) / nn / default: F
	
	@Column(nullable = true, columnDefinition = "TIMESTAMP")
    @Comment("삭제 시간")
    private Timestamp deletedAt; // Timestamp / 삭제 시간 / null
	
    @Column(nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Comment("생성 시간")
	@CreationTimestamp
	private Timestamp createdAt; // Timestamp / 생성 시간 / nn / defualt: NOW()

    @Builder
    public FileUpload(int id, String originName, String saveName, String path, int size, int width, int height, String isDeleted, Timestamp deletedAt) {
        this.id = id;
        this.originName = originName;
        this.saveName = saveName;
        this.path = path;
        this.size = size;
        this.width = width;
        this.height = height;
        this.isDeleted = isDeleted;
        this.deletedAt = deletedAt;
    }
}