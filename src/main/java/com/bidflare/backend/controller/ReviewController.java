package com.bidflare.backend.controller;

import com.bidflare.backend.dto.review.ReviewCreateDto;
import com.bidflare.backend.dto.review.ReviewResponseDto;
import com.bidflare.backend.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ReviewResponseDto> create(@RequestBody ReviewCreateDto dto) {
        return ResponseEntity.ok(reviewService.create(dto));
    }

    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<List<ReviewResponseDto>> getBySeller(@PathVariable UUID sellerId) {
        return ResponseEntity.ok(reviewService.getBySeller(sellerId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        reviewService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
