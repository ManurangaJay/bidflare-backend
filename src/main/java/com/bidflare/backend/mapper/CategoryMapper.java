package com.bidflare.backend.mapper;

import com.bidflare.backend.dto.*;
import com.bidflare.backend.dto.category.CategoryResponseDto;
import com.bidflare.backend.dto.category.CreateCategoryRequestDto;
import com.bidflare.backend.dto.category.UpdateCategoryRequestDto;
import com.bidflare.backend.entity.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public CategoryResponseDto toDto(Category category) {
        return new CategoryResponseDto(
                category.getId(),
                category.getName(),
                category.getSlug()
        );
    }

    public Category toEntity(CreateCategoryRequestDto request) {
        return Category.builder()
                .name(request.name())
                .slug(request.slug())
                .build();
    }

    public void updateEntity(Category category, UpdateCategoryRequestDto request) {
        category.setName(request.name());
        category.setSlug(request.slug());
    }
}
