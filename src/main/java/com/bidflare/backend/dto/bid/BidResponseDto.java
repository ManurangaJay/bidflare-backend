package com.bidflare.backend.dto.bid;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record BidResponseDto(
        UUID id,
        UUID auctionId,
        UUID bidderId,
        BigDecimal amount,
        LocalDateTime createdAt
) {}
