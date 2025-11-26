package com.bidflare.backend.service.impl;

import com.bidflare.backend.dto.product.*;
import com.bidflare.backend.entity.Product;
import com.bidflare.backend.entity.*;
import com.bidflare.backend.event.NotificationEvent;
import com.bidflare.backend.mapper.ProductMapper;
import com.bidflare.backend.repository.*;
import com.bidflare.backend.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepo;
    private final UserRepository userRepo;
    private final CategoryRepository categoryRepo;
    private final ProductMapper productMapper;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public ProductResponseDto createProduct(CreateProductRequestDto request, UUID sellerId) {
        User seller = userRepo.findById(sellerId)
                .orElseThrow(() -> new RuntimeException("Seller not found"));

        Category category = categoryRepo.findById(request.categoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Product product = productMapper.toEntity(request, seller, category);

        eventPublisher.publishEvent(new NotificationEvent(
                sellerId,
                "PRODUCT_CREATED",
                "A new product was created.",
                product.getId()
        ));

        return productMapper.toDto(productRepo.save(product));
    }

    @Override
    public ProductResponseDto updateProduct(UUID id, UpdateProductRequestDto request, UUID sellerId) {
        // Fetch the existing product from the database
        Product product = productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        // Authorize the action (ensure the seller owns the product)
        if (!product.getSeller().getId().equals(sellerId)) {
            throw new RuntimeException("Unauthorized: You do not own this product.");
        }

        // Apply updates conditionally for each non-null field
        if (request.title() != null && !request.title().isBlank()) {
            product.setTitle(request.title());
        }
        if (request.description() != null) {
            product.setDescription(request.description());
        }
        if (request.startingPrice() != null) {
            product.setStartingPrice(request.startingPrice());
        }
        if (request.status() != null) {
            product.setStatus(request.status());
        }
        if (request.categoryId() != null) {
            Category category = categoryRepo.findById(request.categoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found with id: " + request.categoryId()));
            product.setCategory(category);
        }
        // 4. Save the partially updated product and return the DTO
        Product updatedProduct = productRepo.save(product);

        eventPublisher.publishEvent(new NotificationEvent(
                sellerId,
                "PRODUCT_UPDATED",
                "Your product was updated.",
                updatedProduct.getId()
        ));

        return productMapper.toDto(updatedProduct);
    }

    @Override
    public void deleteProduct(UUID id, UUID sellerId) {
        Product product = productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (!product.getSeller().getId().equals(sellerId)) {
            throw new RuntimeException("Unauthorized");
        }

        eventPublisher.publishEvent(new NotificationEvent(
                sellerId,
                "PRODUCT_DELETED",
                "Your product was deleted.",
                product.getId()
        ));
        productRepo.delete(product);
    }

    @Override
    public ProductResponseDto getProductById(UUID id) {
        Product product = productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return productMapper.toDto(product);
    }

    @Override
    public List<ProductResponseDto> getAllProducts() {
        return productRepo.findAll().stream()
                .map(productMapper::toDto)
                .toList();
    }

    @Override
    public List<ProductResponseDto> getProductsBySeller(UUID sellerId) {
        return productRepo.findBySellerId(sellerId).stream()
                .map(productMapper::toDto)
                .toList();
    }

    @Override
    public void markAsDelivered(UUID productId, UUID sellerId) {
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));
        // Update the status to DELIVERED
        product.setStatus(Product.Status.DELIVERED);
        productRepo.save(product);

        eventPublisher.publishEvent(new NotificationEvent(
                sellerId,
                "PRODUCT_DELIVERED",
                "Your product was successfully delivered.",
                product.getId()
                )
        );
    }
}
