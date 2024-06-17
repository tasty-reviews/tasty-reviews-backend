package com.tasty.reviews.tastyreviews.upload.repository;

import com.tasty.reviews.tastyreviews.upload.domain.UploadedFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UploadedFileRepository extends JpaRepository<UploadedFile, Long> {

    // 특정 리뷰 ID에 해당하는 업로드된 파일들을 조회하는 메서드
    List<UploadedFile> findByReviewId(Long reviewId);

}
