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

@Service // Spring에게 이 클래스가 서비스임을 나타냅니다.
@RequiredArgsConstructor // final 필드를 이용하여 생성자를 자동으로 생성합니다.
public class FileUploadService {

    @Value("${file.upload-dir}") // application.properties에서 파일 업로드 경로를 주입받습니다.
    private String uploadDir;

    private final List<String> allowedMimeTypes = List.of("image/jpeg", "image/png", "image/gif"); // 허용되는 MIME 타입 리스트입니다.
    private final UploadedFileRepository uploadedFileRepository; // 파일 업로드 저장소에 대한 의존성 주입

    // 파일 저장 메서드
    public String storeFile(MultipartFile file) throws IOException {
        validateFile(file); // 파일 유효성을 검사합니다.

        String originalFileName = file.getOriginalFilename(); // 업로드된 파일의 원래 이름을 가져옵니다.
        String storedFileName = generateStoredFileName(originalFileName); // 저장할 파일 이름을 생성합니다.

        Path copyLocation = getCopyLocation(storedFileName); // 파일을 저장할 경로를 설정합니다.

        // 파일을 복사합니다.
        Files.copy(file.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);

        return storedFileName; // 저장된 파일 이름을 반환합니다.
    }

    // 파일 유효성 검사 메서드
    private void validateFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) { // 파일이 비어있는지 확인합니다.
            throw new IOException("File is empty");
        }

        String mimeType = file.getContentType(); // 파일의 MIME 타입을 가져옵니다.
        if (!allowedMimeTypes.contains(mimeType)) { // 허용된 MIME 타입인지 확인합니다.
            throw new IOException("Only image files are allowed");
        }
    }

    // 원본 파일명과 UUID를 이용하여 저장 파일명을 생성하는 메서드
    private String generateStoredFileName(String originalFileName) {
        return UUID.randomUUID().toString() + "_" + originalFileName; // UUID와 원본 파일명을 결합하여 저장 파일명을 생성합니다.
    }

    // 파일을 저장할 경로를 설정하는 메서드
    private Path getCopyLocation(String storedFileName) throws IOException {
        Path copyLocation = Paths.get(uploadDir).toAbsolutePath().normalize().resolve(storedFileName); // 절대 경로로 변환하고 표준화합니다.
        Files.createDirectories(copyLocation.getParent()); // 상위 디렉토리를 생성합니다.
        return copyLocation; // 저장 경로를 반환합니다.
    }
}
