package com.tasty.reviews.tastyreviews.review.controller;

import com.tasty.reviews.tastyreviews.review.dto.MemerReviewReadResponseDTO;
import com.tasty.reviews.tastyreviews.review.dto.RestaurantReviewReadDTO;
import com.tasty.reviews.tastyreviews.review.dto.ReviewResponseDTO;
import com.tasty.reviews.tastyreviews.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReviewController {

    private final ReviewService reviewService;
    private final Path fileStorageLocation = Paths.get("uploads").toAbsolutePath().normalize();

    // 특정 레스토랑의 리뷰 조회
    @GetMapping("/restaurants/{restaurantId}")
    public ResponseEntity<List<RestaurantReviewReadDTO>> getReviewsByRestaurantId(@PathVariable(name = "restaurantId") Long restaurantId) {
        List<RestaurantReviewReadDTO> reviews = reviewService.getReviewsByRestaurantId(restaurantId);
        return ResponseEntity.ok(reviews);
    }

    // 특정 회원의 리뷰 조회
    @GetMapping("/members/reviews")
    public ResponseEntity<List<MemerReviewReadResponseDTO>> getReviewsByMemberId() {
        List<MemerReviewReadResponseDTO> reviews = reviewService.getReviewsByMemberId();
        return ResponseEntity.ok(reviews);
    }

    // 리뷰 생성
    @PostMapping("/review/add/{restaurantId}")
    public ResponseEntity<ReviewResponseDTO> addReviewToRestaurant(@PathVariable(name = "restaurantId") Long restaurantId,
                                                                   @RequestParam("comment") String comment,
                                                                   @RequestParam("rating") int rating,
                                                                   @RequestParam(required = false, name = "files") List<MultipartFile> files) {
        ReviewResponseDTO savedReview = reviewService.createReview(restaurantId, comment, rating, files);
        reviewService.restaurantAvgRatingUpdate(savedReview.getRestaurantId());//음식점 별점 반영

        return ResponseEntity.status(HttpStatus.CREATED).body(savedReview);
    }

    // 리뷰 수정
    @PutMapping("/reviews/{reviewId}")
    public ResponseEntity<ReviewResponseDTO> updateReview(@PathVariable Long reviewId,
                                                          @RequestParam("comment") String comment,
                                                          @RequestParam("rating") int rating,
                                                          @RequestParam(value = "files", required = false) List<MultipartFile> files) throws IOException {
        ReviewResponseDTO updatedReview = reviewService.updateReview(reviewId, comment, rating, files);
        return ResponseEntity.ok(updatedReview);
    }

    // 리뷰 삭제
    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long reviewId) {
        reviewService.deleteReview(reviewId);
        return ResponseEntity.noContent().build();
    }

    // 파일 제공 엔드포인트를 정의
    @GetMapping("/reviews/image/{fileName:.+}")
    public ResponseEntity<Resource> getFile(@PathVariable String fileName) {
        try {
            // 파일 이름을 기준으로 파일의 경로를 해결하고 표준화
            Path filePath = fileStorageLocation.resolve(fileName).normalize();
            // 파일 경로를 URL 자원으로 변환
            Resource resource = new UrlResource(filePath.toUri());

            // 자원이 존재하는지 확인
            if (resource.exists()) {
                // 파일의 콘텐츠 타입을 추론
                String contentType = Files.probeContentType(filePath);
                // 콘텐츠 타입이 null인 경우 기본 타입 설정
                if (contentType == null) {
                    contentType = "application/octet-stream";
                }
                // HTTP 응답을 생성하여 자원 반환
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                // 파일이 존재하지 않으면 404 Not Found 응답
                return ResponseEntity.notFound().build();
            }
        } catch (MalformedURLException ex) {
            // URL이 잘못된 경우 500 Internal Server Error 응답
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (IOException ex) {
            // 입출력 예외가 발생한 경우 500 Internal Server Error 응답
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
