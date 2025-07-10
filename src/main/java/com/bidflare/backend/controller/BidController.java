package com.bidflare.backend.controller;

import com.bidflare.backend.dto.bid.BidCreateDto;
import com.bidflare.backend.dto.bid.BidResponseDto;
import com.bidflare.backend.service.BidService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/bids")
@RequiredArgsConstructor
public class BidController {

    private final BidService bidService;

    @PostMapping
    public ResponseEntity<BidResponseDto> createBid(@RequestBody BidCreateDto dto) {
        return ResponseEntity.ok(bidService.createBid(dto));
    }

    @GetMapping("/auction/{auctionId}")
    public ResponseEntity<List<BidResponseDto>> getBidsByAuction(@PathVariable UUID auctionId) {
        return ResponseEntity.ok(bidService.getBidsByAuction(auctionId));
    }
}
