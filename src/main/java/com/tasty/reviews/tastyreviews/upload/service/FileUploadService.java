package com.tasty.reviews.tastyreviews.upload.service;

import com.tasty.reviews.tastyreviews.upload.domain.UploadedFile;
import com.tasty.reviews.tastyreviews.upload.repository.UploadedFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileUploadService {
    @Value("${file.upload-dir}")
    private String uploadDir;

    private final List<String> allowedMimeTypes = List.of("image/jpeg", "image/png", "image/gif");
    private final UploadedFileRepository uploadedFileRepository;

    public UploadedFile storeFile(MultipartFile file) throws IOException {
        // 파일이 비어 있는지 확인
        if (file.isEmpty()) {
            throw new IOException("File is empty");
        }

        // 파일의 MIME 타입을 확인하여 이미지 파일인지 검사
        String mimeType = file.getContentType();
        if (!allowedMimeTypes.contains(mimeType)) {
            throw new IOException("Only image files are allowed");
        }

        // 원본 파일명과 UUID를 이용한 저장 파일명 생성
        String originalFileName = file.getOriginalFilename();
        String storedFileName = UUID.randomUUID().toString() + "_" + originalFileName;

        // 파일 저장 경로 설정
        Path copyLocation = Paths.get(uploadDir).toAbsolutePath().normalize().resolve(storedFileName);

        // 디렉토리가 존재하지 않으면 생성
        Files.createDirectories(copyLocation.getParent());

        // 파일 저장
        Files.copy(file.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);

        // 파일 정보 설정
        UploadedFile uploadedFile = new UploadedFile();
        uploadedFile.setOriginalFileName(originalFileName);
        uploadedFile.setStoredFileName(storedFileName);

        return uploadedFile;
    }
}
