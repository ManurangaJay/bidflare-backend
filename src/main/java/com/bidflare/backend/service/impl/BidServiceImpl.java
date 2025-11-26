package com.bidflare.backend.service.impl;

import com.bidflare.backend.dto.bid.BidCreateDto;
import com.bidflare.backend.dto.bid.BidResponseDto;
import com.bidflare.backend.dto.bid.BidWithAuctionProductDto;
import com.bidflare.backend.dto.productImage.ProductImageDto;
import com.bidflare.backend.entity.*;
import com.bidflare.backend.event.NotificationEvent;
import com.bidflare.backend.mapper.AuctionMapper;
import com.bidflare.backend.mapper.BidMapper;
import com.bidflare.backend.mapper.ProductImageMapper;
import com.bidflare.backend.mapper.ProductMapper;
import com.bidflare.backend.repository.BidRepository;
import com.bidflare.backend.repository.AuctionRepository;
import com.bidflare.backend.repository.ProductImageRepository;
import com.bidflare.backend.repository.UserRepository;
import com.bidflare.backend.service.BidService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BidServiceImpl implements BidService {

    private final BidRepository bidRepository;
    private final AuctionRepository auctionRepository;
    private final UserRepository userRepository;
    private final ProductMapper productMapper;
    private final ProductImageRepository productImageRepository;
    private final ProductImageMapper productImageMapper;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public BidResponseDto createBid(BidCreateDto dto) {
        Auction auction = auctionRepository.findById(dto.auctionId())
                .orElseThrow(() -> new RuntimeException("Auction not found"));

        User bidder = userRepository.findById(dto.bidderId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Bid bid = BidMapper.toEntity(dto, auction, bidder);
        bid = bidRepository.save(bid);

        eventPublisher.publishEvent(new NotificationEvent(
                auction.getProduct().getSeller().getId(),
                "BID_PLACED",
                "A new bid was placed on the auction of " + auction.getProduct().getTitle() + ".",
                auction.getId()
        ));
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

    @Override
    public List<BidWithAuctionProductDto> getBidsByUser(UUID userId) {
        List<Bid> bids = bidRepository.findByBidderId(userId);

        return bids.stream().map(bid -> {
            Auction auction = bid.getAuction();
            Product product = auction.getProduct();

            Optional<ProductImage> imageOpt = productImageRepository.findFirstByProductId(product.getId());
            String imageUrl = imageOpt.map(ProductImage::getImageUrl).orElse(null);

            return new BidWithAuctionProductDto(
                    bid.getId(),
                    bid.getAmount(),
                    bid.getCreatedAt(),
                    AuctionMapper.toDto(auction),
                    productMapper.toDto(product),
                    imageUrl
            );
        }).collect(Collectors.toList());
    }
}
