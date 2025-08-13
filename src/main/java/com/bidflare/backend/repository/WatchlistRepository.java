package com.bidflare.backend.repository;

import com.bidflare.backend.entity.WatchlistItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WatchlistRepository extends JpaRepository<WatchlistItem, UUID> {

    List<WatchlistItem> findByUserId(UUID userId);

    Optional<WatchlistItem> findByUserIdAndProductId(UUID userId, UUID productId);

    void deleteByUserIdAndProductId(UUID userId, UUID productId);

    long countByUserId(UUID userId);
}
