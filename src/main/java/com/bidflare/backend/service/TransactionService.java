package com.bidflare.backend.service;

import com.bidflare.backend.dto.transaction.TransactionCreateDto;
import com.bidflare.backend.dto.transaction.TransactionResponseDto;

import java.util.List;
import java.util.UUID;

public interface TransactionService {
    TransactionResponseDto createTransaction(TransactionCreateDto dto);
    List<TransactionResponseDto> getAll();
    TransactionResponseDto getById(UUID id);
    List<TransactionResponseDto> getByUserId(UUID userId);
    void deleteById(UUID id);
    TransactionResponseDto markAsPaid(UUID id);
}
