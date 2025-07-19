package com.bidflare.backend.controller;

import com.bidflare.backend.dto.transaction.TransactionCreateDto;
import com.bidflare.backend.dto.transaction.TransactionResponseDto;
import com.bidflare.backend.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PreAuthorize("hasAnyRole('ADMIN', 'BUYER', 'SELLER' )")
    @PostMapping
    public ResponseEntity<TransactionResponseDto> create(@RequestBody TransactionCreateDto dto) {
        return ResponseEntity.ok(transactionService.createTransaction(dto));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'BUYER', 'SELLER' )")
    @GetMapping
    public ResponseEntity<List<TransactionResponseDto>> getAll() {
        return ResponseEntity.ok(transactionService.getAll());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'BUYER', 'SELLER' )")
    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponseDto> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(transactionService.getById(id));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'BUYER', 'SELLER' )")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TransactionResponseDto>> getByUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(transactionService.getByUserId(userId));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'BUYER', 'SELLER' )")
    @PatchMapping("/{id}/mark-paid")
    public ResponseEntity<TransactionResponseDto> markAsPaid(@PathVariable UUID id) {
        return ResponseEntity.ok(transactionService.markAsPaid(id));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'BUYER', 'SELLER' )")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        transactionService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
