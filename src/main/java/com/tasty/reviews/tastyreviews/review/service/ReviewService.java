package com.tasty.reviews.tastyreviews.review.service;

import com.tasty.reviews.tastyreviews.member.domain.Member;
import com.tasty.reviews.tastyreviews.restaruant.domain.Restaurant;
import com.tasty.reviews.tastyreviews.review.domain.Review;
import com.tasty.reviews.tastyreviews.member.repository.MemberRepository;
import com.tasty.reviews.tastyreviews.restaruant.repository.RestaurantRepository;
import com.tasty.reviews.tastyreviews.review.dto.ReviewResponseDTO;
import com.tasty.reviews.tastyreviews.review.repository.ReviewRepository;
import com.tasty.reviews.tastyreviews.upload.domain.UploadedFile;
import com.tasty.reviews.tastyreviews.upload.repository.UploadedFileRepository;
import com.tasty.reviews.tastyreviews.upload.service.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final RestaurantRepository restaurantRepository;
    private final MemberRepository memberRepository;
    private final FileUploadService fileUploadService;
    private final UploadedFileRepository uploadedFileRepository;

/*    // 특정 레스토랑의 리뷰 조회
    public List<Review> getReviewsByRestaurantId(Long restaurantId) {
        return reviewRepository.findByRestaurantId(restaurantId);
    }*/

    // 특정 레스토랑의 리뷰 조회
    public List<Review> getReviewsByRestaurantId(Long restaurantId) {
        List<Review> reviews = reviewRepository.findByRestaurantId(restaurantId);
        reviews.forEach(review -> review.setImages(uploadedFileRepository.findByReviewId(review.getId())));
        return reviews;
    }

    // 특정 회원의 리뷰 조회
/*    public List<Review> getReviewsByMemberId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("User must be logged in to access reviews");
        }

        String email = authentication.getName();
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("User not found"));
        return reviewRepository.findByMemberId(member.getId());
    }*/

    // 특정 회원의 리뷰 조회
    public List<Review> getReviewsByMemberId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("User must be logged in to access reviews");
        }

        String email = authentication.getName();
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("User not found"));
        List<Review> reviews = reviewRepository.findByMemberId(member.getId());
        reviews.forEach(review -> review.setImages(uploadedFileRepository.findByReviewId(review.getId())));
        return reviews;
    }

    // 리뷰 생성
    @Transactional
    public ReviewResponseDTO createReview(Long restaurantId, String comment, int rating, List<MultipartFile> files) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("User must be logged in to create a review");
        }

        String email = authentication.getName();
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("User not found"));

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid restaurant ID: " + restaurantId));

        // 먼저 리뷰 객체를 생성하고 기본 정보 설정
        Review review = new Review();
        review.setComment(comment);
        review.setRating(rating);
        review.setRestaurant(restaurant);
        review.setMember(member);

        // 리뷰를 먼저 저장
        Review savedReview = reviewRepository.save(review);

        // 파일 업로드 및 저장된 리뷰에 파일 정보 추가
        List<UploadedFile> uploadedFiles = files.stream()
                .map(file -> {
                    try {
                        UploadedFile uploadedFile = fileUploadService.storeFile(file);
                        uploadedFile.setReview(savedReview); // 리뷰와 연관 설정
                        return uploadedFileRepository.save(uploadedFile); // 파일 저장
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to store file", e);
                    }
                })
                .collect(Collectors.toList());

        // 저장된 리뷰에 파일 리스트 추가
        savedReview.setImages(uploadedFiles);
        reviewRepository.save(savedReview); // 리뷰를 다시 저장하여 파일 정보를 업데이트

        return new ReviewResponseDTO(savedReview);
    }

    // 리뷰 수정
/*    @Transactional
    public Review updateReview(Long reviewId, Review updatedReview) {
        isLogined();

        Review existingReview = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid review ID: " + reviewId));

        existingReview.setRating(updatedReview.getRating());
        existingReview.setComment(updatedReview.getComment());
        existingReview.setImages(updatedReview.getImages());

        return reviewRepository.save(existingReview);
    }*/

    // 리뷰 수정 (이미지 포함)
    @Transactional
    public ReviewResponseDTO updateReview(Long reviewId, String comment, int rating, List<MultipartFile> files) throws IOException {
        isLogined();

        Review existingReview = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid review ID: " + reviewId));

        existingReview.setRating(rating);
        existingReview.setComment(comment);

        if (files != null && !files.isEmpty()) {
            // 기존 파일 삭제
            List<UploadedFile> existingFiles = uploadedFileRepository.findByReviewId(reviewId);
            uploadedFileRepository.deleteAll(existingFiles);

            // 새로운 파일 업로드 및 저장된 리뷰에 파일 정보 추가
            List<UploadedFile> uploadedFiles = files.stream()
                    .map(file -> {
                        try {
                            UploadedFile uploadedFile = fileUploadService.storeFile(file);
                            uploadedFile.setReview(existingReview); // 리뷰와 연관 설정
                            return uploadedFileRepository.save(uploadedFile); // 파일 저장
                        } catch (IOException e) {
                            throw new RuntimeException("Failed to store file", e);
                        }
                    })
                    .collect(Collectors.toList());

            // 리뷰에 새 이미지 리스트 설정
            existingReview.setImages(uploadedFiles);
        }

        Review savedReview = reviewRepository.save(existingReview);
        return new ReviewResponseDTO(savedReview);
    }

    // 리뷰 삭제
    @Transactional
    public void deleteReview(Long reviewId) {
        isLogined();

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid review ID: " + reviewId));

        reviewRepository.delete(review);
    }

    private static void isLogined() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("User must be logged in to perform this action");
        }
    }
}
