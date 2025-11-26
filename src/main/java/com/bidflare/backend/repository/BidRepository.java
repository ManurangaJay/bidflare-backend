package com.bidflare.backend.repository;

import com.bidflare.backend.entity.Bid;
import com.bidflare.backend.entity.Auction;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BidRepository extends JpaRepository<Bid, UUID> {
    List<Bid> findByAuctionOrderByCreatedAtDesc(Auction auction);
    List<Bid> findByBidderId(UUID bidderId);
    Optional<Bid> findTopByAuctionIdOrderByAmountDescCreatedAtAsc(UUID auctionId);
    List<Bid> findByAuctionIdAndBidderIdNot(UUID auctionId, UUID bidderId);

    @Query("SELECT DISTINCT b.bidder.id FROM Bid b WHERE b.auction.id = :auctionId AND b.bidder.id <> :winnerId")
    List<UUID> findDistinctBidderIdsByAuctionIdAndBidderIdNot(UUID auctionId, UUID winnerId);
}
