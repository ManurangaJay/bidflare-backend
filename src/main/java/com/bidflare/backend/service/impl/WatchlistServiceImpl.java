package com.bidflare.backend.service.impl;

import com.bidflare.backend.dto.wishlist.WatchlistDTO;
import com.bidflare.backend.entity.WatchlistItem;
import com.bidflare.backend.mapper.WatchlistMapper;
import com.bidflare.backend.repository.WatchlistRepository;
import com.bidflare.backend.service.WatchlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WatchlistServiceImpl implements WatchlistService {

    private final WatchlistRepository watchlistRepository;
    private final WatchlistMapper mapper;

    @Override
    public List<WatchlistDTO.WishlistResponse> getWishlist(UUID userId) {
        return watchlistRepository.findByUserId(userId).stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public WatchlistDTO.WishlistResponse addToWishlist(UUID userId, WatchlistDTO.WishlistCreateRequest request) {
        watchlistRepository.findByUserIdAndProductId(userId, request.productId())
                .ifPresent(item -> { throw new IllegalStateException("Already in wishlist"); });

        WatchlistItem item = WatchlistItem.builder()
                .userId(userId)
                .productId(request.productId())
                .createdAt(Instant.now())
                .build();

        return mapper.toResponse(watchlistRepository.save(item));
    }

    @Override
    @Transactional
    public void removeFromWishlist(UUID userId, UUID productId) {
        watchlistRepository.deleteByUserIdAndProductId(userId, productId);
    }

    @Override
    public boolean isWishlisted(UUID userId, UUID productId) {
        return watchlistRepository.findByUserIdAndProductId(userId, productId).isPresent();
    }

    @Override
    public long getWishlistCount(UUID userId) {
        return watchlistRepository.countByUserId(userId);
    }
}
