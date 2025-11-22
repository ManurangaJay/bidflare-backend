package com.bidflare.backend.service;

import com.bidflare.backend.dto.auction.*;
import com.bidflare.backend.entity.User;

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

    /**
     *
     * Gets the final auction price in cents, after verifying the user is the winner
     * @param auctionId The ID of the auction
     * @param user The currently authenticated user
     * @return The final  price in cents
     */
    long getAuctionPriceInCents(UUID auctionId, User user);

    /**
     * Marks an auction as paid after successful webhook confirmation
     * @param auctionId the ID of the auction
     */
    void markAuctionAsPaid(UUID auctionId);
}
