package com.bidflare.backend.service.impl;

import com.bidflare.backend.entity.Product;
import com.bidflare.backend.event.NotificationEvent;
import com.bidflare.backend.repository.ProductRepository;
import com.bidflare.backend.dto.transaction.TransactionCreateDto;
import com.bidflare.backend.dto.transaction.TransactionResponseDto;
import com.bidflare.backend.entity.Transaction;
import com.bidflare.backend.mapper.TransactionMapper;
import com.bidflare.backend.repository.TransactionRepository;
import com.bidflare.backend.entity.User;
import com.bidflare.backend.repository.UserRepository;
import com.bidflare.backend.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public TransactionResponseDto createTransaction(TransactionCreateDto dto) {
        User user = userRepository.findById(dto.userId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Product product = productRepository.findById(dto.productId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Transaction transaction = TransactionMapper.toEntity(dto, user, product);

        eventPublisher.publishEvent(new NotificationEvent(
                user.getId(),
                "TRANSACTION_CREATED",
                "A new transaction was created.",
                transaction.getId()
        ));

        return TransactionMapper.toDto(transactionRepository.save(transaction));
    }

    @Override
    public List<TransactionResponseDto> getAll() {
        return transactionRepository.findAll().stream()
                .map(TransactionMapper::toDto)
                .toList();
    }

    @Override
    public TransactionResponseDto getById(UUID id) {
        return transactionRepository.findById(id)
                .map(TransactionMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
    }

    @Override
    public List<TransactionResponseDto> getByUserId(UUID userId) {
        return transactionRepository.findByUserId(userId).stream()
                .map(TransactionMapper::toDto)
                .toList();
    }

    @Override
    public void deleteById(UUID id) {
        transactionRepository.deleteById(id);
    }

    @Override
    public TransactionResponseDto markAsPaid(UUID id) {
        Transaction tx = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
        tx.setStatus(Transaction.Status.PAID);
        tx.setPaidAt(LocalDateTime.now());

        eventPublisher.publishEvent(new NotificationEvent(
                tx.getUser().getId(),
                "TRANSACTION_PAID",
                "Your transaction was successfully paid.",
                tx.getId()
        ));

        eventPublisher.publishEvent(new NotificationEvent(
                tx.getProduct().getSeller().getId(),
                "PRODUCT_SOLD",
                "Your product was successfully sold.",
                tx.getProduct().getId()
        ));
        return TransactionMapper.toDto(transactionRepository.save(tx));
    }
}
