package com.bidflare.backend.service;

import com.bidflare.backend.dto.*;
import com.bidflare.backend.dto.category.CategoryResponseDto;
import com.bidflare.backend.dto.category.CreateCategoryRequestDto;
import com.bidflare.backend.dto.category.UpdateCategoryRequestDto;

import java.util.List;
import java.util.UUID;

public interface CategoryService {
    CategoryResponseDto createCategory(CreateCategoryRequestDto request);
    CategoryResponseDto updateCategory(UUID id, UpdateCategoryRequestDto request);
    void deleteCategory(UUID id);
    CategoryResponseDto getCategoryById(UUID id);
    List<CategoryResponseDto> getAllCategories();
}
