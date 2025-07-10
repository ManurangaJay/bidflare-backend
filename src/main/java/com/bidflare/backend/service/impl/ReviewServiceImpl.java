package com.bidflare.backend.service.impl;

import com.bidflare.backend.dto.review.ReviewCreateDto;
import com.bidflare.backend.dto.review.ReviewResponseDto;
import com.bidflare.backend.entity.Review;
import com.bidflare.backend.mapper.ReviewMapper;
import com.bidflare.backend.repository.ReviewRepository;
import com.bidflare.backend.entity.User;
import com.bidflare.backend.repository.UserRepository;
import com.bidflare.backend.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    @Override
    public ReviewResponseDto create(ReviewCreateDto dto) {
        User reviewer = userRepository.findById(dto.reviewerId())
                .orElseThrow(() -> new RuntimeException("Reviewer not found"));
        User seller = userRepository.findById(dto.sellerId())
                .orElseThrow(() -> new RuntimeException("Seller not found"));

        Review review = ReviewMapper.toEntity(dto, reviewer, seller);
        return ReviewMapper.toDto(reviewRepository.save(review));
    }

    @Override
    public List<ReviewResponseDto> getBySeller(UUID sellerId) {
        return reviewRepository.findBySellerIdOrderByCreatedAtDesc(sellerId).stream()
                .map(ReviewMapper::toDto)
                .toList();
    }

    @Override
    public void delete(UUID id) {
        reviewRepository.deleteById(id);
    }
}
