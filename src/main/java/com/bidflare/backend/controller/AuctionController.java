package com.bidflare.backend.controller;

import com.bidflare.backend.dto.auction.*;
import com.bidflare.backend.service.AuctionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/auctions")
@RequiredArgsConstructor
public class AuctionController {

    private final AuctionService auctionService;

    @PostMapping
    public ResponseEntity<AuctionResponseDto> createAuction(@RequestBody AuctionCreateDto dto) {
        return ResponseEntity.ok(auctionService.createAuction(dto));
    }

    @GetMapping
    public ResponseEntity<List<AuctionResponseDto>> getAllAuctions() {
        return ResponseEntity.ok(auctionService.getAllAuctions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuctionResponseDto> getAuctionById(@PathVariable UUID id) {
        return ResponseEntity.ok(auctionService.getAuctionById(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<AuctionResponseDto> updateAuction(@PathVariable UUID id, @RequestBody AuctionUpdateDto dto) {
        return ResponseEntity.ok(auctionService.updateAuction(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuction(@PathVariable UUID id) {
        auctionService.deleteAuction(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<AuctionResponseDto>> getAuctionsByProductId(@PathVariable UUID productId) {
        return ResponseEntity.ok(auctionService.getAuctionsByProductId(productId));
    }
}
