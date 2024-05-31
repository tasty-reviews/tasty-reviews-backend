package com.tasty.reviews.tastyreviews.upload.service;

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

    public String  storeFile(MultipartFile file) throws IOException {
        validateFile(file);

        String originalFileName = file.getOriginalFilename();
        String storedFileName = generateStoredFileName(originalFileName);

        Path copyLocation = getCopyLocation(storedFileName);

        Files.copy(file.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);

        return storedFileName;
    }

    private void validateFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IOException("File is empty");
        }

        String mimeType = file.getContentType();
        if (!allowedMimeTypes.contains(mimeType)) {
            throw new IOException("Only image files are allowed");
        }
    }

    // 원본 파일명과 UUID를 이용한 저장 파일명 생성
    private String generateStoredFileName(String originalFileName) {
        return UUID.randomUUID().toString() + "_" + originalFileName;
    }

    // 파일 저장 경로 설정
    private Path getCopyLocation(String storedFileName) throws IOException {
        Path copyLocation = Paths.get(uploadDir).toAbsolutePath().normalize().resolve(storedFileName);
        Files.createDirectories(copyLocation.getParent());
        return copyLocation;
    }

}
