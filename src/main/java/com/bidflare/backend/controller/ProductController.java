package com.bidflare.backend.controller;

import com.bidflare.backend.dto.*;
import com.bidflare.backend.dto.product.CreateProductRequestDto;
import com.bidflare.backend.dto.product.ProductResponseDto;
import com.bidflare.backend.dto.product.UpdateProductRequestDto;
import com.bidflare.backend.security.jwt.JwtUtil;
import com.bidflare.backend.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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

    @PostMapping
    public ResponseEntity<ProductResponseDto> create(@RequestBody CreateProductRequestDto request,
                                                  Principal principal) {
        UUID sellerId = UUID.fromString(jwtUtil.extractUserIdFromPrincipal(principal));
        return ResponseEntity.ok(productService.createProduct(request, sellerId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDto> update(@PathVariable UUID id,
                                                  @RequestBody UpdateProductRequestDto request,
                                                  Principal principal) {
        UUID sellerId = UUID.fromString(jwtUtil.extractUserIdFromPrincipal(principal));
        return ResponseEntity.ok(productService.updateProduct(id, request, sellerId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id,
                                       Principal principal) {
        UUID sellerId = UUID.fromString(jwtUtil.extractUserIdFromPrincipal(principal));
        productService.deleteProduct(id, sellerId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @GetMapping
    public ResponseEntity<List<ProductResponseDto>> getAll() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/seller")
    public ResponseEntity<List<ProductResponseDto>> getSellerProducts(Principal principal) {
        UUID sellerId = UUID.fromString(jwtUtil.extractUserIdFromPrincipal(principal));
        return ResponseEntity.ok(productService.getProductsBySeller(sellerId));
    }
}
