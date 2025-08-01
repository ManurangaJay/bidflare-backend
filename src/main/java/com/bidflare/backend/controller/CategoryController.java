package com.bidflare.backend.controller;

import com.bidflare.backend.dto.*;
import com.bidflare.backend.dto.category.CategoryResponseDto;
import com.bidflare.backend.dto.category.CreateCategoryRequestDto;
import com.bidflare.backend.dto.category.UpdateCategoryRequestDto;
import com.bidflare.backend.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping
    public ResponseEntity<CategoryResponseDto> create(@RequestBody CreateCategoryRequestDto request) {
        return ResponseEntity.ok(categoryService.createCategory(request));
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> update(@PathVariable UUID id,
                                                      @RequestBody UpdateCategoryRequestDto request) {
        return ResponseEntity.ok(categoryService.updateCategory(id, request));
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'BUYER', 'SELLER')")
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'BUYER', 'SELLER')")
    @GetMapping
    public ResponseEntity<List<CategoryResponseDto>> getAll() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }
}
