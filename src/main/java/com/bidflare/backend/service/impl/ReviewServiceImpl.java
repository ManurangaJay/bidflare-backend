package com.bidflare.backend.service.impl;

import com.bidflare.backend.dto.review.ReviewCreateDto;
import com.bidflare.backend.dto.review.ReviewResponseDto;
import com.bidflare.backend.entity.Product;
import com.bidflare.backend.entity.Review;
import com.bidflare.backend.event.NotificationEvent;
import com.bidflare.backend.mapper.ReviewMapper;
import com.bidflare.backend.repository.ProductRepository;
import com.bidflare.backend.repository.ReviewRepository;
import com.bidflare.backend.entity.User;
import com.bidflare.backend.repository.UserRepository;
import com.bidflare.backend.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public ReviewResponseDto create(ReviewCreateDto dto) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User reviewer = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Reviewer not found"));
        User seller = userRepository.findById(dto.sellerId())
                .orElseThrow(() -> new RuntimeException("Seller not found"));
        Product product = productRepository.findById(dto.productId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Review review = ReviewMapper.toEntity(dto, reviewer, seller, product);
        reviewRepository.save(review);

        eventPublisher.publishEvent(new NotificationEvent(
                seller.getId(),
                "NEW_REVIEW",
                "You have received a new review on your product " + product.getTitle(),
                review.getId()
        ));

        return ReviewMapper.toDto(review);
    }

    @Override
    public List<ReviewResponseDto> getBySeller(UUID sellerId) {
        return reviewRepository.findBySellerIdOrderByCreatedAtDesc(sellerId).stream()
                .map(ReviewMapper::toDto)
                .toList();
    }

    @Override
    public List<ReviewResponseDto> getByProduct(UUID productId) {
        return reviewRepository.findByProductIdOrderByCreatedAtDesc(productId).stream()
                .map(ReviewMapper::toDto)
                .toList();
    }

    @Override
    public void delete(UUID id) {
        reviewRepository.deleteById(id);
    }
}
