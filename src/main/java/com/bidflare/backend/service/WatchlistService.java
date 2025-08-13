package com.bidflare.backend.service;


import com.bidflare.backend.dto.wishlist.WatchlistDTO;

import java.util.List;
import java.util.UUID;

public interface WatchlistService {

    List<WatchlistDTO.WishlistResponse> getWishlist(UUID userId);

    WatchlistDTO.WishlistResponse addToWishlist(UUID userId, WatchlistDTO.WishlistCreateRequest request);

    void removeFromWishlist(UUID userId, UUID productId);

    boolean isWishlisted(UUID userId, UUID productId);

    long getWishlistCount(UUID userId);
}
