package com.bidflare.backend.service;

import com.bidflare.backend.dto.auction.*;

import java.util.List;
import java.util.UUID;

public interface AuctionService {
    AuctionResponseDto createAuction(AuctionCreateDto dto);
    List<AuctionResponseDto> getAllAuctions();
    AuctionResponseDto getAuctionById(UUID id);
    AuctionResponseDto updateAuction(UUID id, AuctionUpdateDto dto);
    void deleteAuction(UUID id);
    List<AuctionResponseDto> getAuctionsByProductId(UUID productId);
    List<AuctionResponseDto> getAuctionsByWinnerId(UUID winnerId);
}
