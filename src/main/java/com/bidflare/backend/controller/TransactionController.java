package com.bidflare.backend.controller;

import com.bidflare.backend.dto.transaction.TransactionCreateDto;
import com.bidflare.backend.dto.transaction.TransactionResponseDto;
import com.bidflare.backend.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<TransactionResponseDto> create(@RequestBody TransactionCreateDto dto) {
        return ResponseEntity.ok(transactionService.createTransaction(dto));
    }

    @GetMapping
    public ResponseEntity<List<TransactionResponseDto>> getAll() {
        return ResponseEntity.ok(transactionService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponseDto> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(transactionService.getById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TransactionResponseDto>> getByUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(transactionService.getByUserId(userId));
    }

    @PatchMapping("/{id}/mark-paid")
    public ResponseEntity<TransactionResponseDto> markAsPaid(@PathVariable UUID id) {
        return ResponseEntity.ok(transactionService.markAsPaid(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        transactionService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
