package com.bidflare.backend.dto.auction;

import java.time.ZonedDateTime;
import java.util.UUID;

public record AuctionUpdateDto(
        ZonedDateTime startTime,
        ZonedDateTime endTime,
        Boolean isClosed,
        UUID winnerId
) {}
