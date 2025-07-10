package com.bidflare.backend.mapper;

import com.bidflare.backend.dto.review.ReviewCreateDto;
import com.bidflare.backend.dto.review.ReviewResponseDto;
import com.bidflare.backend.entity.Review;
import com.bidflare.backend.entity.User;

import java.time.LocalDateTime;

public class ReviewMapper {

    public static Review toEntity(ReviewCreateDto dto, User reviewer, User seller) {
        return Review.builder()
                .reviewer(reviewer)
                .seller(seller)
                .rating(dto.rating())
                .comment(dto.comment())
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static ReviewResponseDto toDto(Review r) {
        return new ReviewResponseDto(
                r.getId(),
                r.getReviewer().getId(),
                r.getSeller().getId(),
                r.getRating(),
                r.getComment(),
                r.getCreatedAt()
        );
    }
}
