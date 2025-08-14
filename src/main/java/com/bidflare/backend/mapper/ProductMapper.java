package com.bidflare.backend.mapper;

import com.bidflare.backend.dto.product.CreateProductRequestDto;
import com.bidflare.backend.dto.product.ProductResponseDto;
import com.bidflare.backend.dto.product.UpdateProductRequestDto;
import com.bidflare.backend.entity.Category;
import com.bidflare.backend.entity.Product;
import com.bidflare.backend.entity.User;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class ProductMapper {

    public Product toEntity(CreateProductRequestDto request, User seller, Category category) {
        return Product.builder()
                .title(request.title())
                .description(request.description())
                .startingPrice(request.startingPrice())
                .category(category)
                .seller(seller)
                .status(Product.Status.DRAFT)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();
    }

    public void updateEntity(Product product, UpdateProductRequestDto request, Category category) {
        product.setTitle(request.title());
        product.setDescription(request.description());
        product.setStartingPrice(request.startingPrice());
        product.setStatus(Product.Status.valueOf(String.valueOf(request.status())));
        product.setCategory(category);
        product.setUpdatedAt(Instant.now());
    }

    public ProductResponseDto toDto(Product product) {
        return new ProductResponseDto(
                product.getId(),
                product.getTitle(),
                product.getDescription(),
                product.getStartingPrice(),
                product.getStatus().name(),
                product.getSeller().getId(),
                product.getCategory().getId(),
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
    }
}
