package com.bidflare.backend.repository;

import com.bidflare.backend.entity.Bid;
import com.bidflare.backend.entity.Auction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BidRepository extends JpaRepository<Bid, UUID> {
    List<Bid> findByAuctionOrderByCreatedAtDesc(Auction auction);
}
