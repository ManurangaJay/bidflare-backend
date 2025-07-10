package com.bidflare.backend.service.impl;

import com.bidflare.backend.dto.auction.*;
import com.bidflare.backend.entity.Auction;
import com.bidflare.backend.mapper.AuctionMapper;
import com.bidflare.backend.repository.AuctionRepository;
import com.bidflare.backend.entity.Product;
import com.bidflare.backend.repository.ProductRepository;
import com.bidflare.backend.entity.User;
import com.bidflare.backend.repository.UserRepository;
import com.bidflare.backend.service.AuctionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuctionServiceImpl implements AuctionService {

    private final AuctionRepository auctionRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Override
    public AuctionResponseDto createAuction(AuctionCreateDto dto) {
        Product product = productRepository.findById(dto.productId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

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
                .orElseThrow(() -> new RuntimeException("Auction not found"));
        return AuctionMapper.toDto(auction);
    }

    @Override
    public AuctionResponseDto updateAuction(UUID id, AuctionUpdateDto dto) {
        Auction auction = auctionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Auction not found"));

        User winner = null;
        if (dto.winnerId() != null) {
            winner = userRepository.findById(dto.winnerId())
                    .orElseThrow(() -> new RuntimeException("Winner not found"));
        }

        AuctionMapper.updateAuction(auction, dto, winner);
        return AuctionMapper.toDto(auctionRepository.save(auction));
    }

    @Override
    public void deleteAuction(UUID id) {
        Auction auction = auctionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Auction not found"));
        auctionRepository.delete(auction);
    }
}
