package com.tasty.reviews.tastyreviews.upload.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tasty.reviews.tastyreviews.review.domain.Review;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UploadedFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "review_id", nullable = false)
    @JsonIgnore
    private Review review;

    private String originalFileName;
    private String storedFileName;
}
