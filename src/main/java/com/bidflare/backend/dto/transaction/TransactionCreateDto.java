package com.bidflare.backend.dto.transaction;

import java.math.BigDecimal;
import java.util.UUID;

public record TransactionCreateDto(
        UUID userId,
        UUID productId,
        BigDecimal amount,
        String paymentMethod
) {}
