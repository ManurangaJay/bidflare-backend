package com.bidflare.backend.service.impl;

import com.bidflare.backend.dto.*;
import com.bidflare.backend.dto.product.CreateProductRequestDto;
import com.bidflare.backend.dto.product.ProductResponseDto;
import com.bidflare.backend.dto.product.UpdateProductRequestDto;
import com.bidflare.backend.entity.*;
import com.bidflare.backend.repository.*;
import com.bidflare.backend.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepo;
    private final UserRepository userRepo;
    private final CategoryRepository categoryRepo;

    @Override
    public ProductResponseDto createProduct(CreateProductRequestDto request, UUID sellerId) {
        User seller = userRepo.findById(sellerId)
                .orElseThrow(() -> new RuntimeException("Seller not found"));

        Category category = categoryRepo.findById(request.categoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Product product = Product.builder()
                .title(request.title())
                .description(request.description())
                .startingPrice(request.startingPrice())
                .category(category)
                .status(Product.Status.DRAFT)
                .seller(seller)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        product = productRepo.save(product);

        return mapToDto(product);
    }

    @Override
    public ProductResponseDto updateProduct(UUID id, UpdateProductRequestDto request, UUID sellerId) {
        Product product = productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (!product.getSeller().getId().equals(sellerId)) {
            throw new RuntimeException("Unauthorized");
        }

        Category category = categoryRepo.findById(request.categoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        product.setTitle(request.title());
        product.setDescription(request.description());
        product.setStartingPrice(request.startingPrice());
        product.setCategory(category);
        product.setStatus(Product.Status.valueOf(request.status()));
        product.setUpdatedAt(Instant.now());

        return mapToDto(productRepo.save(product));
    }

    @Override
    public void deleteProduct(UUID id, UUID sellerId) {
        Product product = productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (!product.getSeller().getId().equals(sellerId)) {
            throw new RuntimeException("Unauthorized");
        }

        productRepo.delete(product);
    }

    @Override
    public ProductResponseDto getProductById(UUID id) {
        Product product = productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return mapToDto(product);
    }

    @Override
    public List<ProductResponseDto> getAllProducts() {
        return productRepo.findAll().stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    public List<ProductResponseDto> getProductsBySeller(UUID sellerId) {
        return productRepo.findBySellerId(sellerId).stream()
                .map(this::mapToDto)
                .toList();
    }

    private ProductResponseDto mapToDto(Product product) {
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
