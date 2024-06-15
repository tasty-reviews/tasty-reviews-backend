package com.tasty.reviews.tastyreviews.upload.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tasty.reviews.tastyreviews.review.domain.Review;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity // JPA 엔티티로 표시
@Data // Lombok을 사용하여 getter, setter, toString, equals, hashCode 메서드 자동 생성
@NoArgsConstructor // 파라미터가 없는 기본 생성자 자동 생성
@AllArgsConstructor // 모든 필드를 포함하는 생성자 자동 생성
public class UploadedFile {

    @Id // 기본 키로 설정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본 키 생성을 데이터베이스에 위임 (Auto Increment)
    private Long id;

    @ManyToOne // 다대일 관계를 설정 (여러 파일이 하나의 리뷰에 속함)
    @JoinColumn(name = "review_id", nullable = false) // 조인 컬럼을 설정 (외래 키)
    @JsonIgnore // 직렬화 시 해당 필드를 무시 (순환 참조 방지)
    private Review review;

    private String originalFileName; // 업로드된 파일의 원래 이름
    private String storedFileName; // 서버에 저장된 파일 이름
}

