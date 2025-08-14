package com.bidflare.backend.controller;

import com.bidflare.backend.dto.auction.*;
import com.bidflare.backend.service.AuctionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/auctions")
@RequiredArgsConstructor
public class AuctionController {

    private final AuctionService auctionService;
    private final com.bidflare.backend.security.jwt.JwtUtil jwtUtil;

    @PreAuthorize("hasAnyRole('ADMIN','SELLER' )")
    @PostMapping
    public ResponseEntity<AuctionResponseDto> createAuction(@RequestBody AuctionCreateDto dto) {
        return ResponseEntity.ok(auctionService.createAuction(dto));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'BUYER', 'SELLER' )")
    @GetMapping
    public ResponseEntity<List<AuctionResponseDto>> getAllAuctions() {
        return ResponseEntity.ok(auctionService.getAllAuctions());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'BUYER', 'SELLER')")
    @GetMapping("/{id}")
    public ResponseEntity<AuctionResponseDto> getAuctionById(@PathVariable UUID id) {
        return ResponseEntity.ok(auctionService.getAuctionById(id));
    }

    @PreAuthorize("hasAnyRole('ADMIN','SELLER')")
    @PatchMapping("/{id}")
    public ResponseEntity<AuctionResponseDto> updateAuction(@PathVariable UUID id, @RequestBody AuctionUpdateDto dto) {
        return ResponseEntity.ok(auctionService.updateAuction(id, dto));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SELLER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuction(@PathVariable UUID id) {
        auctionService.deleteAuction(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'BUYER', 'SELLER')")
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<AuctionResponseDto>> getAuctionsByProductId(@PathVariable UUID productId) {
        return ResponseEntity.ok(auctionService.getAuctionsByProductId(productId));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'BUYER')")
    @GetMapping("/won")
    public ResponseEntity<List<AuctionResponseDto>> getWonAuctions() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = jwtUtil.extractUserIdFromAuthentication(authentication);
        UUID winnerId = UUID.fromString(userId);

        return ResponseEntity.ok(auctionService.getAuctionsByWinnerId(winnerId));
    }
}
