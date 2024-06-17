package com.tasty.reviews.tastyreviews.upload.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tasty.reviews.tastyreviews.review.domain.Review;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity // JPA 엔티티로 선언
@Data // Lombok을 사용하여 getter, setter, toString, equals, hashCode 메서드 자동 생성
@NoArgsConstructor // 파라미터가 없는 기본 생성자 자동 생성
@AllArgsConstructor // 모든 필드를 포함하는 생성자 자동 생성
public class UploadedFile {

    @Id // 기본 키로 설정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 데이터베이스에서 자동 생성되는 기본 키 설정
    private Long id;

    @ManyToOne // 다대일 관계 설정: 여러 개의 파일이 하나의 리뷰에 속함
    @JoinColumn(name = "review_id", nullable = false) // 외래 키 설정
    @JsonIgnore // JSON 직렬화 시 무시되도록 설정 (순환 참조 방지)
    private Review review; // 이 파일이 속한 리뷰 엔티티

    private String originalFileName; // 업로드된 파일의 원래 이름
    private String storedFileName; // 서버에 저장된 파일 이름
}
