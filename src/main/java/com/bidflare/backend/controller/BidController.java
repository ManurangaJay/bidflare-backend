package com.bidflare.backend.controller;

import com.bidflare.backend.dto.bid.BidCreateDto;
import com.bidflare.backend.dto.bid.BidResponseDto;
import com.bidflare.backend.dto.bid.BidWithAuctionProductDto;
import com.bidflare.backend.service.BidService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/bids")
@RequiredArgsConstructor
public class BidController {

    private final BidService bidService;
    private final com.bidflare.backend.security.jwt.JwtUtil jwtUtil;

    @PreAuthorize("hasAnyRole('ADMIN','BUYER')")
    @PostMapping
    public ResponseEntity<BidResponseDto> createBid(@RequestBody BidCreateDto dto) {
        return ResponseEntity.ok(bidService.createBid(dto));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'BUYER', 'SELLER')")
    @GetMapping("/auction/{auctionId}")
    public ResponseEntity<List<BidResponseDto>> getBidsByAuction(@PathVariable UUID auctionId) {
        return ResponseEntity.ok(bidService.getBidsByAuction(auctionId));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'BUYER')")
    @GetMapping("/user")
    public ResponseEntity<List<BidWithAuctionProductDto>> getBidsByUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = jwtUtil.extractUserIdFromAuthentication(authentication);
        UUID buyerId = UUID.fromString(userId);

        return ResponseEntity.ok(bidService.getBidsByUser(buyerId));
    }
}
