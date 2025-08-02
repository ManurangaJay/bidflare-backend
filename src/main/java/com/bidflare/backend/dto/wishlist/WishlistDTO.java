package com.bidflare.backend.dto.wishlist;

import java.time.Instant;
import java.util.UUID;

public class WishlistDTO {

    // Create DTO
    public record WishlistCreateRequest(UUID productId) {}

    // Response DTO
    public record WishlistResponse(UUID id, UUID productId, Instant createdAt) {}
}
