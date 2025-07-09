package com.bidflare.backend.controller;

import com.bidflare.backend.dto.productImage.CreateProductImageDto;
import com.bidflare.backend.dto.productImage.ProductImageDto;
import com.bidflare.backend.service.ProductImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/product-images")
@RequiredArgsConstructor
public class ProductImageController {

    private final ProductImageService productImageService;

    @PostMapping
    public ResponseEntity<ProductImageDto> uploadImage(
            @RequestParam UUID productId,
            @RequestParam MultipartFile imageFile
    ) {
        CreateProductImageDto dto = CreateProductImageDto.builder()
                .productId(productId)
                .imageFile(imageFile)
                .build();

        return ResponseEntity.ok(productImageService.addImage(dto));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<List<ProductImageDto>> getImagesByProduct(@PathVariable UUID productId) {
        return ResponseEntity.ok(productImageService.getImagesByProductId(productId));
    }

    @DeleteMapping("/{imageId}")
    public ResponseEntity<Void> deleteImage(@PathVariable UUID imageId) {
        productImageService.deleteImage(imageId);
        return ResponseEntity.noContent().build();
    }
}
