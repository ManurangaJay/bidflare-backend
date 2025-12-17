package com.bidflare.backend.dto.auction;

import java.time.ZonedDateTime;
import java.util.UUID;

public record AuctionCreateDto(
        UUID productId,
        ZonedDateTime startTime,
        ZonedDateTime endTime
) {}
