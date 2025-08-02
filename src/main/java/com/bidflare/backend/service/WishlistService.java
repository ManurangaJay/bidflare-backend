package com.bidflare.backend.service;


import com.bidflare.backend.dto.wishlist.WishlistDTO;

import java.util.List;
import java.util.UUID;

public interface WishlistService {

    List<WishlistDTO.WishlistResponse> getWishlist(UUID userId);

    WishlistDTO.WishlistResponse addToWishlist(UUID userId, WishlistDTO.WishlistCreateRequest request);

    void removeFromWishlist(UUID userId, UUID productId);

    boolean isWishlisted(UUID userId, UUID productId);

    long getWishlistCount(UUID userId);
}
