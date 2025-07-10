package com.bidflare.backend.dto.auction;

import java.time.LocalDateTime;
import java.util.UUID;

public record AuctionResponseDto(
        UUID id,
        UUID productId,
        LocalDateTime startTime,
        LocalDateTime endTime,
        boolean isClosed,
        UUID winnerId
) {}
