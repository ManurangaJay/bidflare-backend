package com.bidflare.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "wishlist_items",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "product_id"})},
        indexes = {
                @Index(name = "idx_wishlist_user_id", columnList = "user_id"),
                @Index(name = "idx_wishlist_product_id", columnList = "product_id")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WatchlistItem {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "product_id", nullable = false)
    private UUID productId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;
}
