package com.bidflare.backend.mapper;

import com.bidflare.backend.dto.bid.BidCreateDto;
import com.bidflare.backend.dto.bid.BidResponseDto;
import com.bidflare.backend.entity.Bid;
import com.bidflare.backend.entity.Auction;
import com.bidflare.backend.entity.User;

import java.time.LocalDateTime;

public class BidMapper {

    public static Bid toEntity(BidCreateDto dto, Auction auction, User bidder) {
        return Bid.builder()
                .auction(auction)
                .bidder(bidder)
                .amount(dto.amount())
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static BidResponseDto toDto(Bid bid) {
        return new BidResponseDto(
                bid.getId(),
                bid.getAuction().getId(),
                bid.getBidder().getId(),
                bid.getAmount(),
                bid.getCreatedAt()
        );
    }
}
