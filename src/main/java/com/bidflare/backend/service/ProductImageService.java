package com.bidflare.backend.service;

import com.bidflare.backend.dto.productImage.CreateProductImageDto;
import com.bidflare.backend.dto.productImage.ProductImageDto;
import java.util.List;
import java.util.UUID;

public interface ProductImageService {
    ProductImageDto addImage(CreateProductImageDto dto);
    List<ProductImageDto> getImagesByProductId(UUID productId);
    void deleteImage(UUID id);
}
