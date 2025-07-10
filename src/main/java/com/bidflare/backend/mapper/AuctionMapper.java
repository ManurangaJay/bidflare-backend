package com.bidflare.backend.mapper;

import com.bidflare.backend.dto.auction.*;
import com.bidflare.backend.entity.Auction;
import com.bidflare.backend.dto.auction.AuctionResponseDto;
import com.bidflare.backend.entity.Product;
import com.bidflare.backend.entity.User;

public class AuctionMapper {

    public static AuctionResponseDto toDto(Auction auction) {
        return new AuctionResponseDto(
                auction.getId(),
                auction.getProduct().getId(),
                auction.getStartTime(),
                auction.getEndTime(),
                auction.isClosed(),
                auction.getWinner() != null ? auction.getWinner().getId() : null
        );
    }

    public static Auction toEntity(AuctionCreateDto dto, Product product) {
        return Auction.builder()
                .product(product)
                .startTime(dto.startTime())
                .endTime(dto.endTime())
                .isClosed(false)
                .build();
    }

    public static void updateAuction(Auction auction, AuctionUpdateDto dto, User winner) {
        if (dto.startTime() != null) auction.setStartTime(dto.startTime());
        if (dto.endTime() != null) auction.setEndTime(dto.endTime());
        if (dto.isClosed() != null) auction.setClosed(dto.isClosed());
        if (dto.winnerId() != null) auction.setWinner(winner);
    }
}
