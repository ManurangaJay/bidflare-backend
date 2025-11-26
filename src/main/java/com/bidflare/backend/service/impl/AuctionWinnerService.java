package com.bidflare.backend.service.impl;

import com.bidflare.backend.entity.Auction;
import com.bidflare.backend.entity.Bid;
import com.bidflare.backend.entity.Product;
import com.bidflare.backend.event.NotificationEvent;
import com.bidflare.backend.repository.AuctionRepository;
import com.bidflare.backend.repository.BidRepository;
import com.bidflare.backend.repository.ProductRepository;
import com.bidflare.backend.service.NotificationService;
import com.bidflare.backend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuctionWinnerService {

    @Autowired
    private ApplicationEventPublisher eventPublisher;
    @Autowired
    private AuctionRepository auctionRepository;
    @Autowired
    private BidRepository bidRepository;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private ProductRepository productRepository;

    /**
     * This method runs every minute to process auctions that have ended.
     * The @Transactional annotation ensures that all database operations within this method
     * either complete successfully or roll back together, maintaining data consistency.
     */
    @Scheduled(fixedRate = 60000) // Runs every 60,000 milliseconds (1 minute)
    @Transactional
    public void processEndedAuctions() {
        System.out.println("Running scheduled job: Processing ended auctions...");

        // Find auctions that have ended but are not yet closed
        List<Auction> endedAuctions = auctionRepository.findAllByEndTimeBeforeAndIsClosedFalse(LocalDateTime.now());

        if (endedAuctions.isEmpty()) {
            return; // No auctions to process
        }

        for (Auction auction : endedAuctions) {
            // Find the highest bid for the auction.
            Optional<Bid> winningBid = bidRepository.findTopByAuctionIdOrderByAmountDescCreatedAtAsc(auction.getId());

            if (winningBid.isPresent()) {
                // A winner was found
                Bid winner = winningBid.get();
                auction.setWinner(winner.getBidder()); // Set the winner_id

                // Set the last price from the winning bid's amount
                auction.setLastPrice(winner.getAmount());

                Product product = auction.getProduct();
                if (product!= null){
                    product.setStatus(Product.Status.SOLD);
                    productRepository.save(product);
                }

                System.out.println("Auction ID: " + auction.getId() + " won by User ID: " + winner.getBidder().getId() + " with price: " + winner.getAmount());


                // Notify the winner
                eventPublisher.publishEvent(new NotificationEvent(
                        winner.getBidder().getId(),
                        "AUCTION_WON",
                        "Congratulations! You have won the auction on the product " + product.getTitle() + ".",
                        auction.getId()
                ));

                // Notify the seller
                eventPublisher.publishEvent(new NotificationEvent(
                        auction.getProduct().getSeller().getId(),
                        "AUCTION_WON_BY",
                        "Your auction on the product " + product.getTitle() + " was won by " + winner.getBidder().getName()+ ".",
                        auction.getId()
                ));

                // Notify the losers
                List<UUID> loserIds = bidRepository.findDistinctBidderIdsByAuctionIdAndBidderIdNot(auction.getId(), winner.getBidder().getId());

                if (!loserIds.isEmpty()) {
                    eventPublisher.publishEvent(new NotificationEvent(
                            loserIds,
                            "AUCTION_LOST",
                            "Unfortunately, you were outbid in the auction for " + product.getTitle() + ".",
                            auction.getId()
                    ));
                    System.out.println("Notified " + loserIds.size() + " losing bidders for auction " + auction.getId());
                }
            } else {
                // No bids were placed on this auction
                System.out.println("Auction ID: " + auction.getId() + " closed with no bids.");
                // lastPrice remains null, which is correct
                auction.setWinner(null);
            }

            // Mark the auction as closed
            auction.setClosed(true);
            auctionRepository.save(auction);
        }
    }
}