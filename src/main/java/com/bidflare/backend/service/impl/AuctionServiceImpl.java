package com.bidflare.backend.service.impl;

import com.bidflare.backend.dto.auction.*;
import com.bidflare.backend.entity.Auction;
import com.bidflare.backend.exception.AuctionNotFoundException;
import com.bidflare.backend.exception.InvalidAuctionStateException;
import com.bidflare.backend.mapper.AuctionMapper;
import com.bidflare.backend.repository.AuctionRepository;
import com.bidflare.backend.entity.Product;
import com.bidflare.backend.repository.ProductRepository;
import com.bidflare.backend.entity.User;
import com.bidflare.backend.repository.UserRepository;
import com.bidflare.backend.service.AuctionService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.security.access.AccessDeniedException;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuctionServiceImpl implements AuctionService {

    // Logger for logging payment events
    private static final Logger logger = LoggerFactory.getLogger(AuctionServiceImpl.class);
    private final AuctionRepository auctionRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Override
    public AuctionResponseDto createAuction(AuctionCreateDto dto) {
        Product product = productRepository.findById(dto.productId())
                .orElseThrow(() -> new AuctionNotFoundException("Product not found with id: " + dto.productId()));

        Auction auction = AuctionMapper.toEntity(dto, product);
        return AuctionMapper.toDto(auctionRepository.save(auction));
    }

    @Override
    public List<AuctionResponseDto> getAllAuctions() {
        return auctionRepository.findAll().stream()
                .map(AuctionMapper::toDto)
                .toList();
    }

    @Override
    public AuctionResponseDto getAuctionById(UUID id) {
        Auction auction = auctionRepository.findById(id)
                .orElseThrow(() -> new AuctionNotFoundException("Auction not found with id: " + id));
        return AuctionMapper.toDto(auction);
    }

    @Override
    public AuctionResponseDto updateAuction(UUID id, AuctionUpdateDto dto) {
        Auction auction = auctionRepository.findById(id)
                .orElseThrow(() -> new AuctionNotFoundException("Auction not found with id: " + id));

        User winner = null;
        if (dto.winnerId() != null) {
            winner = userRepository.findById(dto.winnerId())
                    .orElseThrow(() -> new AuctionNotFoundException("Winner (User) not found with id: " + dto.winnerId()));
        }

        AuctionMapper.updateAuction(auction, dto, winner);
        return AuctionMapper.toDto(auctionRepository.save(auction));
    }

    @Override
    public void deleteAuction(UUID id) {
        Auction auction = auctionRepository.findById(id)
                .orElseThrow(() -> new AuctionNotFoundException("Auction not found with id: " + id));
        auctionRepository.delete(auction);
    }
    @Override
    public List<AuctionResponseDto> getAuctionsByProductId(UUID productId) {
        List<Auction> auctions = auctionRepository.findByProductId(productId);
        return auctions.stream()
                .map(AuctionMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuctionResponseDto> getAuctionsByWinnerId(UUID winnerId) {
        List<Auction> auctions = auctionRepository.findByWinnerId(winnerId);
        return auctions.stream()
                .map(AuctionMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public long getAuctionPriceInCents(UUID auctionId, User user) {
        // Find the auction
        Auction auction = auctionRepository.findById(auctionId).orElseThrow(() -> new AuctionNotFoundException("Auction not found: " + auctionId));

        // Security Check
        if(auction.getWinner() == null){
            logger.warn("Payment attempt on auction {} with no winner", auctionId);
            throw new InvalidAuctionStateException("Auction has no winner: " + auctionId);
        }

        // Ensure the authenticated user is the winner
        if(!auction.getWinner().getId().equals(user.getId())){
            logger.warn("Security violation: User {} tried to pay for the auction won by {}", user.getId(), auction.getWinner().getId() );
            throw new AccessDeniedException("You are not the winner of this auction");
        }
        
        // Get the price
        BigDecimal lastPrice = auction.getLastPrice();
        if(lastPrice == null || lastPrice.signum() <=0){
            logger.error("Auction {} has an invalid last price: {}", auctionId, lastPrice);
            throw new InvalidAuctionStateException("Auction has an invalid price");
        }

        // convert to LKR
        BigDecimal cents = lastPrice.multiply(new BigDecimal(100));
        return cents.longValue();
    }



    @Override
    @Transactional
    public void markAuctionAsPaid(UUID auctionId) {
        // Find the auction
        Auction auction = auctionRepository.findById(auctionId).orElseThrow(()-> new AuctionNotFoundException("Cannot mark as paid. Auction not found for id: " + auctionId));

        // Get the associated product
        Product product = auction.getProduct();
        if (product == null) {
            throw new InvalidAuctionStateException("Data integrity issue: Auction " + auctionId + " has no associated product.");
        }

        // Check current status to avoid double-processing
        if (product.getStatus() == Product.Status.PAID) {
            logger.info("Product {} is already marked as PAID. Skipping update." , product.getId());
            return;
        }

        // Update the status
        logger.info("Marking product {} (Title: {}) as PAID.", product.getId(), product.getTitle());
        product.setStatus(Product.Status.PAID);

        // Save the product
        productRepository.save(product);
    }
}
