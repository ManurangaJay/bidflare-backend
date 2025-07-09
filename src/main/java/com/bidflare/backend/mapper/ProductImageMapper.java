package com.bidflare.backend.mapper;

import com.bidflare.backend.dto.productImage.ProductImageDto;
import com.bidflare.backend.entity.ProductImage;
import org.springframework.stereotype.Component;

@Component
public class ProductImageMapper {

    public ProductImageDto toDto(ProductImage image) {
        return ProductImageDto.builder()
                .id(image.getId())
                .productId(image.getProduct().getId())
                .imageUrl(image.getImageUrl())
                .build();
    }
}
