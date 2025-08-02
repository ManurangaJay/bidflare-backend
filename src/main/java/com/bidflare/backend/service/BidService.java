package com.bidflare.backend.service;

import com.bidflare.backend.dto.bid.BidCreateDto;
import com.bidflare.backend.dto.bid.BidResponseDto;
import com.bidflare.backend.dto.bid.BidWithAuctionProductDto;

import java.util.List;
import java.util.UUID;

public interface BidService {
    BidResponseDto createBid(BidCreateDto dto);
    List<BidResponseDto> getBidsByAuction(UUID auctionId);
    public List<BidWithAuctionProductDto> getBidsByUser(UUID userId);
}
