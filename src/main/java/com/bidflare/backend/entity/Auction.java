package com.bidflare.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal; // Import BigDecimal for monetary values
import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Table(name = "auctions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Auction {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "start_time", nullable = false)
    private ZonedDateTime startTime;

    @Column(name = "end_time", nullable = false)
    private ZonedDateTime endTime;

    @Column(name = "last_price", precision = 19, scale = 2)
    private BigDecimal lastPrice;

    @Column(name = "is_closed", nullable = false)
    private boolean isClosed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "winner_id")
    private User winner;
}