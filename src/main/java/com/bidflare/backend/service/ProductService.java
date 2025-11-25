package com.bidflare.backend.service;

import com.bidflare.backend.dto.*;
import com.bidflare.backend.dto.product.CreateProductRequestDto;
import com.bidflare.backend.dto.product.ProductResponseDto;
import com.bidflare.backend.dto.product.UpdateProductRequestDto;

import java.util.List;
import java.util.UUID;

public interface ProductService {
    ProductResponseDto createProduct(CreateProductRequestDto request, UUID sellerId);
    ProductResponseDto updateProduct(UUID id, UpdateProductRequestDto request, UUID sellerId);
    void deleteProduct(UUID id, UUID sellerId);
    ProductResponseDto getProductById(UUID id);
    List<ProductResponseDto> getAllProducts();
    List<ProductResponseDto> getProductsBySeller(UUID sellerId);
    void markAsDelivered(UUID productId, UUID sellerId);
}
