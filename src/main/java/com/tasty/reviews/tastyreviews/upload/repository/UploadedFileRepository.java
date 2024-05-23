package com.tasty.reviews.tastyreviews.upload.repository;

import com.tasty.reviews.tastyreviews.upload.domain.UploadedFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UploadedFileRepository extends JpaRepository<UploadedFile, Long> {
}