package com.bidflare.backend.dto.auction;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

public record AuctionResponseDto(
        UUID id,
        UUID productId,
        ZonedDateTime startTime,
        ZonedDateTime endTime,
        boolean isClosed,
        UUID winnerId,
        BigDecimal lastPrice
) {}
