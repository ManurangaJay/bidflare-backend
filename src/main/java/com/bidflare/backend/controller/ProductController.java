package com.bidflare.backend.controller;

import com.bidflare.backend.dto.*;
import com.bidflare.backend.dto.product.CreateProductRequestDto;
import com.bidflare.backend.dto.product.ProductResponseDto;
import com.bidflare.backend.dto.product.UpdateProductRequestDto;
import com.bidflare.backend.security.jwt.JwtUtil;
import com.bidflare.backend.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final JwtUtil jwtUtil;

    @PreAuthorize("hasAnyRole('ADMIN', 'SELLER')")
    @PostMapping
    public ResponseEntity<ProductResponseDto> create(@RequestBody CreateProductRequestDto request,
                                                  Principal principal) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = jwtUtil.extractUserIdFromAuthentication(authentication);
        UUID sellerId = UUID.fromString(userId);
        return ResponseEntity.ok(productService.createProduct(request, sellerId));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SELLER', 'BUYER')")
    @PatchMapping("/{id}")
    public ResponseEntity<ProductResponseDto> update(@PathVariable UUID id,
                                                     @RequestBody UpdateProductRequestDto request,
                                                     Principal principal) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = jwtUtil.extractUserIdFromAuthentication(authentication);
        UUID sellerId = UUID.fromString(userId);
        return ResponseEntity.ok(productService.updateProduct(id, request, sellerId));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SELLER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id,
                                       Principal principal) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = jwtUtil.extractUserIdFromAuthentication(authentication);
        UUID sellerId = UUID.fromString(userId);
        productService.deleteProduct(id, sellerId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'BUYER', 'SELLER')")
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'BUYER', 'SELLER')")
    @GetMapping
    public ResponseEntity<List<ProductResponseDto>> getAll() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'BUYER', 'SELLER')")
    @GetMapping("/seller")
    public ResponseEntity<List<ProductResponseDto>> getSellerProducts(Principal principal) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = jwtUtil.extractUserIdFromAuthentication(authentication);
        UUID sellerId = UUID.fromString(userId);
        return ResponseEntity.ok(productService.getProductsBySeller(sellerId));
    }

    @PreAuthorize("hasAnyRole('ADMIN','BUYER')")
    @PatchMapping("/{id}/deliver")
    public ResponseEntity<Void> markAsDelivered(@PathVariable UUID id,
                                                Principal principal) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = jwtUtil.extractUserIdFromAuthentication(authentication);
        UUID sellerId = UUID.fromString(userId);
        productService.markAsDelivered(id, sellerId);
        return ResponseEntity.ok().build();
    }
}
