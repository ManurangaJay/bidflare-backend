package com.bidflare.backend.dto.auction;

import java.time.LocalDateTime;
import java.util.UUID;

public record AuctionUpdateDto(
        LocalDateTime startTime,
        LocalDateTime endTime,
        Boolean isClosed,
        UUID winnerId
) {}
