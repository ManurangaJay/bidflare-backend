package com.bidflare.backend.dto.auction;

import java.time.LocalDateTime;
import java.util.UUID;

public record AuctionCreateDto(
        UUID productId,
        LocalDateTime startTime,
        LocalDateTime endTime
) {}
