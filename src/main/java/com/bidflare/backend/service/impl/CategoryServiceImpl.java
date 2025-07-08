package com.bidflare.backend.service.impl;

import com.bidflare.backend.dto.category.CategoryResponseDto;
import com.bidflare.backend.dto.category.CreateCategoryRequestDto;
import com.bidflare.backend.dto.category.UpdateCategoryRequestDto;
import com.bidflare.backend.entity.Category;
import com.bidflare.backend.mapper.CategoryMapper;
import com.bidflare.backend.repository.CategoryRepository;
import com.bidflare.backend.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepo;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryResponseDto createCategory(CreateCategoryRequestDto request) {
        if (categoryRepo.existsByName(request.name()) || categoryRepo.existsBySlug(request.slug())) {
            throw new RuntimeException("Category name or slug already exists");
        }

        Category category = categoryMapper.toEntity(request);
        return categoryMapper.toDto(categoryRepo.save(category));
    }

    @Override
    public CategoryResponseDto updateCategory(UUID id, UpdateCategoryRequestDto request) {
        Category category = categoryRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        categoryMapper.updateEntity(category, request);
        return categoryMapper.toDto(categoryRepo.save(category));
    }

    @Override
    public void deleteCategory(UUID id) {
        Category category = categoryRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        categoryRepo.delete(category);
    }

    @Override
    public CategoryResponseDto getCategoryById(UUID id) {
        return categoryRepo.findById(id)
                .map(categoryMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Category not found"));
    }

    @Override
    public List<CategoryResponseDto> getAllCategories() {
        return categoryRepo.findAll().stream()
                .map(categoryMapper::toDto)
                .toList();
    }
}
