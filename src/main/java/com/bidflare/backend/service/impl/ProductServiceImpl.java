package com.bidflare.backend.service.impl;

import com.bidflare.backend.dto.product.*;
import com.bidflare.backend.entity.*;
import com.bidflare.backend.mapper.ProductMapper;
import com.bidflare.backend.repository.*;
import com.bidflare.backend.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepo;
    private final UserRepository userRepo;
    private final CategoryRepository categoryRepo;
    private final ProductMapper productMapper;

    @Override
    public ProductResponseDto createProduct(CreateProductRequestDto request, UUID sellerId) {
        User seller = userRepo.findById(sellerId)
                .orElseThrow(() -> new RuntimeException("Seller not found"));

        Category category = categoryRepo.findById(request.categoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Product product = productMapper.toEntity(request, seller, category);
        return productMapper.toDto(productRepo.save(product));
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

        productMapper.updateEntity(product, request, category);
        return productMapper.toDto(productRepo.save(product));
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
}
