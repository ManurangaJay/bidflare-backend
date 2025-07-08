package com.bidflare.backend.service.impl;

import com.bidflare.backend.dto.category.CategoryResponseDto;
import com.bidflare.backend.dto.category.CreateCategoryRequestDto;
import com.bidflare.backend.dto.category.UpdateCategoryRequestDto;
import com.bidflare.backend.entity.Category;
import com.bidflare.backend.repository.CategoryRepository;
import com.bidflare.backend.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepo;

    @Override
    public CategoryResponseDto createCategory(CreateCategoryRequestDto request) {
        if (categoryRepo.existsByName(request.name()) || categoryRepo.existsBySlug(request.slug())) {
            throw new RuntimeException("Category name or slug already exists");
        }

        Category category = Category.builder()
                .name(request.name())
                .slug(request.slug())
                .build();

        return mapToDto(categoryRepo.save(category));
    }

    @Override
    public CategoryResponseDto updateCategory(UUID id, UpdateCategoryRequestDto request) {
        Category category = categoryRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        category.setName(request.name());
        category.setSlug(request.slug());

        return mapToDto(categoryRepo.save(category));
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
                .map(this::mapToDto)
                .orElseThrow(() -> new RuntimeException("Category not found"));
    }

    @Override
    public List<CategoryResponseDto> getAllCategories() {
        return categoryRepo.findAll().stream()
                .map(this::mapToDto)
                .toList();
    }

    private CategoryResponseDto mapToDto(Category category) {
        return new CategoryResponseDto(
                category.getId(),
                category.getName(),
                category.getSlug()
        );
    }
}
