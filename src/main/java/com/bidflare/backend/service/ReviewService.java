package com.bidflare.backend.service;

import com.bidflare.backend.dto.review.ReviewCreateDto;
import com.bidflare.backend.dto.review.ReviewResponseDto;

import java.util.List;
import java.util.UUID;

public interface ReviewService {
    ReviewResponseDto create(ReviewCreateDto dto);
    List<ReviewResponseDto> getBySeller(UUID sellerId);
    List<ReviewResponseDto> getByProduct(UUID productId);
    void delete(UUID id);
}
