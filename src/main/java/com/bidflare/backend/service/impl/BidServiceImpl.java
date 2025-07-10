package com.bidflare.backend.service.impl;

import com.bidflare.backend.dto.bid.BidCreateDto;
import com.bidflare.backend.dto.bid.BidResponseDto;
import com.bidflare.backend.entity.Bid;
import com.bidflare.backend.mapper.BidMapper;
import com.bidflare.backend.repository.BidRepository;
import com.bidflare.backend.entity.Auction;
import com.bidflare.backend.repository.AuctionRepository;
import com.bidflare.backend.entity.User;
import com.bidflare.backend.repository.UserRepository;
import com.bidflare.backend.service.BidService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BidServiceImpl implements BidService {

    private final BidRepository bidRepository;
    private final AuctionRepository auctionRepository;
    private final UserRepository userRepository;

    @Override
    public BidResponseDto createBid(BidCreateDto dto) {
        Auction auction = auctionRepository.findById(dto.auctionId())
                .orElseThrow(() -> new RuntimeException("Auction not found"));

        User bidder = userRepository.findById(dto.bidderId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Bid bid = BidMapper.toEntity(dto, auction, bidder);
        bid = bidRepository.save(bid);

        return BidMapper.toDto(bid);
    }

    @Override
    public List<BidResponseDto> getBidsByAuction(UUID auctionId) {
        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new RuntimeException("Auction not found"));

        return bidRepository.findByAuctionOrderByCreatedAtDesc(auction)
                .stream()
                .map(BidMapper::toDto)
                .toList();
    }
}
