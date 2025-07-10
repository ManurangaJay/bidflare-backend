package com.bidflare.backend.mapper;

import com.bidflare.backend.entity.Product;
import com.bidflare.backend.dto.transaction.TransactionCreateDto;
import com.bidflare.backend.dto.transaction.TransactionResponseDto;
import com.bidflare.backend.entity.Transaction;
import com.bidflare.backend.entity.User;

import java.time.LocalDateTime;

public class TransactionMapper {

    public static Transaction toEntity(TransactionCreateDto dto, User user, Product product) {
        return Transaction.builder()
                .user(user)
                .product(product)
                .amount(dto.amount())
                .paymentMethod(dto.paymentMethod())
                .status(Transaction.Status.PENDING)
                .paidAt(null)
                .build();
    }

    public static TransactionResponseDto toDto(Transaction tx) {
        return new TransactionResponseDto(
                tx.getId(),
                tx.getUser().getId(),
                tx.getProduct().getId(),
                tx.getAmount(),
                tx.getStatus(),
                tx.getPaymentMethod(),
                tx.getPaidAt()
        );
    }
}
