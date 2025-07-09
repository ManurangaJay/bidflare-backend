package com.bidflare.backend.dto.productImage;

import lombok.*;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductImageDto {
    private UUID id;
    private UUID productId;
    private String imageUrl;
}
