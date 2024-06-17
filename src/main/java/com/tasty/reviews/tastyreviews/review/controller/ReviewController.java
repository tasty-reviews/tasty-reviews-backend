package com.tasty.reviews.tastyreviews.review.controller;

import com.tasty.reviews.tastyreviews.review.dto.MemerReviewReadResponseDTO;
import com.tasty.reviews.tastyreviews.review.dto.RestaurantReviewReadDTO;
import com.tasty.reviews.tastyreviews.review.dto.ReviewResponseDTO;
import com.tasty.reviews.tastyreviews.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * 리뷰 관련 API를 제공하는 컨트롤러 클래스
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReviewController {

    private final ReviewService reviewService;
    private final Path fileStorageLocation = Paths.get("uploads").toAbsolutePath().normalize();

    /**
     * 특정 레스토랑의 리뷰를 조회하는 엔드포인트
     *
     * @param restaurantId 조회할 레스토랑의 ID
     * @return 조회된 레스토랑의 리뷰 목록
     */
    @GetMapping("/restaurants/{restaurantId}")
    public ResponseEntity<List<RestaurantReviewReadDTO>> getReviewsByRestaurantId(@PathVariable(name = "restaurantId") Long restaurantId) {
        List<RestaurantReviewReadDTO> reviews = reviewService.getReviewsByRestaurantId(restaurantId);
        return ResponseEntity.ok(reviews);
    }

    /**
     * 특정 회원의 리뷰를 조회하는 엔드포인트
     *
     * @return 조회된 회원의 리뷰 목록
     */
    @GetMapping("/members/reviews")
    public ResponseEntity<List<MemerReviewReadResponseDTO>> getReviewsByMemberId() {
        List<MemerReviewReadResponseDTO> reviews = reviewService.getReviewsByMemberId();
        return ResponseEntity.ok(reviews);
    }

    /**
     * 특정 레스토랑에 리뷰를 추가하는 엔드포인트
     *
     * @param restaurantId 추가할 리뷰의 레스토랑 ID
     * @param comment      리뷰 내용
     * @param rating       평점
     * @param files        첨부 파일 리스트
     * @return 추가된 리뷰 정보 DTO
     */
    @PostMapping("/review/add/{restaurantId}")
    public ResponseEntity<ReviewResponseDTO> addReviewToRestaurant(@PathVariable(name = "restaurantId") Long restaurantId,
                                                                   @RequestParam("comment") String comment,
                                                                   @RequestParam("rating") int rating,
                                                                   @RequestParam(required = false, name = "files") List<MultipartFile> files) {
        ReviewResponseDTO savedReview = reviewService.createReview(restaurantId, comment, rating, files);
        reviewService.restaurantAvgRatingUpdate(savedReview.getRestaurantId()); // 음식점 평점 업데이트

        return ResponseEntity.status(HttpStatus.CREATED).body(savedReview);
    }

    /**
     * 리뷰를 수정하는 엔드포인트
     *
     * @param reviewId 리뷰 ID
     * @param comment  수정할 리뷰 내용
     * @param rating   수정할 평점
     * @param files    수정할 첨부 파일 리스트
     * @return 수정된 리뷰 정보 DTO
     * @throws IOException 파일 처리 중 발생할 수 있는 예외
     */
    @PutMapping("/reviews/{reviewId}")
    public ResponseEntity<ReviewResponseDTO> updateReview(@PathVariable Long reviewId,
                                                          @RequestParam("comment") String comment,
                                                          @RequestParam("rating") int rating,
                                                          @RequestParam(value = "files", required = false) List<MultipartFile> files) throws IOException {
        ReviewResponseDTO updatedReview = reviewService.updateReview(reviewId, comment, rating, files);
        return ResponseEntity.ok(updatedReview);
    }

    /**
     * 리뷰를 삭제하는 엔드포인트
     *
     * @param reviewId 삭제할 리뷰의 ID
     * @return 응답 상태 코드만 반환 (204 No Content)
     */
    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long reviewId) {
        reviewService.deleteReview(reviewId);
        return ResponseEntity.noContent().build();
    }

    /**
     * 파일을 제공하는 엔드포인트
     *
     * @param fileName 제공할 파일 이름
     * @return 파일 리소스 응답
     */
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
