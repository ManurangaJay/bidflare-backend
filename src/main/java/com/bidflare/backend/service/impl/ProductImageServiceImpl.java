package com.bidflare.backend.service.impl;

import com.bidflare.backend.dto.productImage.CreateProductImageDto;
import com.bidflare.backend.dto.productImage.ProductImageDto;
import com.bidflare.backend.entity.Product;
import com.bidflare.backend.entity.ProductImage;
import com.bidflare.backend.mapper.ProductImageMapper;
import com.bidflare.backend.repository.ProductImageRepository;
import com.bidflare.backend.repository.ProductRepository;
import com.bidflare.backend.service.ProductImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductImageServiceImpl implements ProductImageService {

    private final ProductImageRepository imageRepository;
    private final ProductRepository productRepository;
    private final ProductImageMapper mapper;

    private final String UPLOAD_DIR = new File("src/main/resources/static/images").getAbsolutePath();

    @Override
    public ProductImageDto addImage(CreateProductImageDto dto) {
        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        MultipartFile file = dto.getImageFile();
        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
        String filePath = UPLOAD_DIR + "/" + filename;

        try {
            Files.createDirectories(Paths.get(UPLOAD_DIR));
            file.transferTo(new File(filePath));
        } catch (IOException e) {
            throw new RuntimeException("Failed to save image" + e.getMessage());
        }

        ProductImage image = ProductImage.builder()
                .product(product)
                .imageUrl("/images/" + filename)
                .build();

        return mapper.toDto(imageRepository.save(image));
    }

    @Override
    public List<ProductImageDto> getImagesByProductId(UUID productId) {
        return imageRepository.findByProductId(productId)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteImage(UUID id) {
        ProductImage image = imageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Image not found"));

        try {
            Files.deleteIfExists(Paths.get(image.getImageUrl()));
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete file", e);
        }

        imageRepository.deleteById(id);
    }
}
