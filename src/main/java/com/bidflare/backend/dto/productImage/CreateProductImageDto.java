package com.bidflare.backend.dto.productImage;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateProductImageDto {
    private UUID productId;
    private MultipartFile imageFile;
}
