package com.bidflare.backend.repository;

import com.bidflare.backend.entity.Auction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

public interface AuctionRepository extends JpaRepository<Auction, UUID> {
    List<Auction> findByProductId(UUID productId);
    List<Auction> findAllByEndTimeBeforeAndIsClosedFalse(ZonedDateTime endTime);
    List<Auction> findByWinnerId(UUID winnerId);
}
