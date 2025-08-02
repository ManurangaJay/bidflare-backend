package com.bidflare.backend.dto.bid;

import com.bidflare.backend.dto.auction.AuctionResponseDto;
import com.bidflare.backend.dto.product.ProductResponseDto;
import com.bidflare.backend.dto.productImage.ProductImageDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BidWithAuctionProductDto {
    private UUID id;
    private BigDecimal amount;
    private LocalDateTime createdAt;
    private AuctionResponseDto auction;
    private ProductResponseDto product;
    private String  image;
}
