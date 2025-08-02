package com.bidflare.backend.repository;

import com.bidflare.backend.entity.WishlistItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WishlistRepository extends JpaRepository<WishlistItem, UUID> {

    List<WishlistItem> findByUserId(UUID userId);

    Optional<WishlistItem> findByUserIdAndProductId(UUID userId, UUID productId);

    void deleteByUserIdAndProductId(UUID userId, UUID productId);

    long countByUserId(UUID userId);
}
