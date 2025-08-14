package com.bidflare.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "seller_id", nullable = false)
    private User seller;

    private String title;
    @Column(columnDefinition = "TEXT")
    private String description;

    private BigDecimal startingPrice;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Enumerated(EnumType.STRING)
    private Status status;

    private Instant createdAt;
    private Instant updatedAt;

    public enum Status {
        DRAFT, LISTED, SOLD, PAID, SHIPPED, DELIVERED
    }
}
