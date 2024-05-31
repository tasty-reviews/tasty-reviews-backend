package com.tasty.reviews.tastyreviews.review.service;

import com.tasty.reviews.tastyreviews.member.domain.Member;
import com.tasty.reviews.tastyreviews.member.repository.MemberRepository;
import com.tasty.reviews.tastyreviews.restaruant.domain.Restaurant;
import com.tasty.reviews.tastyreviews.restaruant.repository.RestaurantRepository;
import com.tasty.reviews.tastyreviews.review.domain.Review;
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

    private Member getAuthenticatedMember() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("User must be logged in");
        }
        String email = authentication.getName();
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("User not found"));
    }

    // 특정 레스토랑의 리뷰 조회
    public List<Review> getReviewsByRestaurantId(Long restaurantId) {
        List<Review> reviews = reviewRepository.findByRestaurantId(restaurantId);
        reviews.forEach(review -> review.setImages(uploadedFileRepository.findByReviewId(review.getId())));
        return reviews;
    }


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
        Member member = getAuthenticatedMember();
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid restaurant ID: " + restaurantId));

        Review review = Review.builder()
                .comment(comment)
                .rating(rating)
                .restaurant(restaurant)
                .member(member)
                .build();
        restaurant.setReviewCount(restaurant.getReviewCount() + 1);
        Review savedReview = reviewRepository.save(review);

        List<UploadedFile> uploadedFiles = files.stream()
                .map(file -> {
                    try {
                        String storedFileName = fileUploadService.storeFile(file);
                        UploadedFile uploadedFile = new UploadedFile();
                        uploadedFile.setOriginalFileName(file.getOriginalFilename());
                        uploadedFile.setStoredFileName(storedFileName);
                        uploadedFile.setReview(savedReview);
                        return uploadedFileRepository.save(uploadedFile);
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to store file", e);
                    }
                })
                .collect(Collectors.toList());

        savedReview.setImages(uploadedFiles);
        reviewRepository.save(savedReview);
        return new ReviewResponseDTO(savedReview);
    }



    // 리뷰 수정 (이미지 포함)
    @Transactional
    public ReviewResponseDTO updateReview(Long reviewId, String comment, int rating, List<MultipartFile> files) throws IOException {
        isLogined();

        Review existingReview = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid review ID: " + reviewId));

        existingReview.setRating(rating);
        existingReview.setComment(comment);

        if (files != null && !files.isEmpty()) {
            uploadedFileRepository.deleteAll(uploadedFileRepository.findByReviewId(reviewId));
            List<UploadedFile> uploadedFiles = files.stream()
                    .map(file -> {
                        try {
                            String storedFileName = fileUploadService.storeFile(file);
                            UploadedFile uploadedFile = new UploadedFile();
                            uploadedFile.setOriginalFileName(file.getOriginalFilename());
                            uploadedFile.setStoredFileName(storedFileName);
                            uploadedFile.setReview(existingReview);
                            return uploadedFileRepository.save(uploadedFile);
                        } catch (IOException e) {
                            throw new RuntimeException("Failed to store file", e);
                        }
                    })
                    .collect(Collectors.toList());

            existingReview.setImages(uploadedFiles);
        }

        return new ReviewResponseDTO(reviewRepository.save(existingReview));
    }

    //리뷰 삭제
    @Transactional
    public void deleteReview(Long reviewId) {
        isLogined();

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid review ID: " + reviewId));

        reviewRepository.delete(review);
    }

    private static void isLogined() {
        // 사용자의 인증 정보를 가져옴
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 사용자가 로그인되어 있는지 확인
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("User must be logged in to create a review");
        }
    }

    @Transactional
    public void restaurantAgvRatingUpdate(Long restaurantId) {

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 음식점이 없습니다"));

        //레스토랑에서 리뷰 가져오기
        List<Review> reviews = restaurant.getReviews();

        int totalRating = 0;
        int reviewCount = restaurant.getReviewCount();

        for (Review review : reviews) {
            totalRating += review.getRating();
        }

        restaurant.updateRating(totalRating, reviewCount);
    }
}
