package com.bidflare.backend.dto.wishlist;

import com.bidflare.backend.dto.auction.AuctionResponseDto;
import com.bidflare.backend.dto.product.ProductResponseDto;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
public class WishlistDetailedResponse {
    private UUID wishlistId;
    private AuctionResponseDto auction;
    private ProductResponseDto product;
    private String imageUrl;
    private Instant wishlistedAt;
}
