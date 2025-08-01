package com.bidflare.backend.repository;

import com.bidflare.backend.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ReviewRepository extends JpaRepository<Review, UUID> {
    List<Review> findBySellerIdOrderByCreatedAtDesc(UUID sellerId);
}
