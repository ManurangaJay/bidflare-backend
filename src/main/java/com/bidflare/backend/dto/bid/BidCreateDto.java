package com.bidflare.backend.dto.bid;

import java.math.BigDecimal;
import java.util.UUID;

public record BidCreateDto(
        UUID auctionId,
        UUID bidderId,
        BigDecimal amount
) {}
