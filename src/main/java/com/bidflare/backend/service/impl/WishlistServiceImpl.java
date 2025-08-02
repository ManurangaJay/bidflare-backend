package com.bidflare.backend.service.impl;

import com.bidflare.backend.dto.wishlist.WishlistDTO;
import com.bidflare.backend.entity.WishlistItem;
import com.bidflare.backend.mapper.WishlistMapper;
import com.bidflare.backend.repository.WishlistRepository;
import com.bidflare.backend.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WishlistServiceImpl implements WishlistService {

    private final WishlistRepository wishlistRepository;
    private final WishlistMapper mapper;

    @Override
    public List<WishlistDTO.WishlistResponse> getWishlist(UUID userId) {
        return wishlistRepository.findByUserId(userId).stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public WishlistDTO.WishlistResponse addToWishlist(UUID userId, WishlistDTO.WishlistCreateRequest request) {
        wishlistRepository.findByUserIdAndProductId(userId, request.productId())
                .ifPresent(item -> { throw new IllegalStateException("Already in wishlist"); });

        WishlistItem item = WishlistItem.builder()
                .userId(userId)
                .productId(request.productId())
                .createdAt(Instant.now())
                .build();

        return mapper.toResponse(wishlistRepository.save(item));
    }

    @Override
    @Transactional
    public void removeFromWishlist(UUID userId, UUID productId) {
        wishlistRepository.deleteByUserIdAndProductId(userId, productId);
    }

    @Override
    public boolean isWishlisted(UUID userId, UUID productId) {
        return wishlistRepository.findByUserIdAndProductId(userId, productId).isPresent();
    }

    @Override
    public long getWishlistCount(UUID userId) {
        return wishlistRepository.countByUserId(userId);
    }
}
