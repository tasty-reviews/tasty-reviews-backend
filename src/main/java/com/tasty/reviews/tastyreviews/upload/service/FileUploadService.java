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

@Service // 서비스 클래스임을 나타냄
@RequiredArgsConstructor // final 필드에 대해 생성자를 자동으로 생성
public class FileUploadService {
    @Value("${file.upload-dir}") // 파일 업로드 경로를 application.properties에서 주입받음
    private String uploadDir;

    private final List<String> allowedMimeTypes = List.of("image/jpeg", "image/png", "image/gif"); // 허용되는 MIME 타입 리스트
    private final UploadedFileRepository uploadedFileRepository; // 파일 업로드 저장소 의존성 주입

    // 파일 저장 메서드
    public String storeFile(MultipartFile file) throws IOException {
        validateFile(file); // 파일 유효성 검사

        String originalFileName = file.getOriginalFilename(); // 원본 파일명 가져오기
        String storedFileName = generateStoredFileName(originalFileName); // 저장 파일명 생성

        Path copyLocation = getCopyLocation(storedFileName); // 파일 저장 경로 설정

        Files.copy(file.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING); // 파일 복사

        return storedFileName; // 저장된 파일명 반환
    }

    // 파일 유효성 검사 메서드
    private void validateFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) { // 파일이 비어있는지 확인
            throw new IOException("File is empty");
        }

        String mimeType = file.getContentType(); // 파일의 MIME 타입 가져오기
        if (!allowedMimeTypes.contains(mimeType)) { // 허용된 MIME 타입인지 확인
            throw new IOException("Only image files are allowed");
        }
    }

    // 원본 파일명과 UUID를 이용한 저장 파일명 생성
    private String generateStoredFileName(String originalFileName) {
        return UUID.randomUUID().toString() + "_" + originalFileName; // UUID와 원본 파일명을 결합하여 저장 파일명 생성
    }

    // 파일 저장 경로 설정
    private Path getCopyLocation(String storedFileName) throws IOException {
        Path copyLocation = Paths.get(uploadDir).toAbsolutePath().normalize().resolve(storedFileName); // 절대 경로로 변환하고 표준화
        Files.createDirectories(copyLocation.getParent()); // 상위 디렉토리 생성
        return copyLocation; // 저장 경로 반환
    }
}
