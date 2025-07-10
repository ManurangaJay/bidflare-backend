package com.bidflare.backend.dto.transaction;

import com.bidflare.backend.entity.Transaction.Status;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record TransactionResponseDto(
        UUID id,
        UUID userId,
        UUID productId,
        BigDecimal amount,
        Status status,
        String paymentMethod,
        LocalDateTime paidAt
) {}
