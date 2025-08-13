package com.bidflare.backend.controller;

import com.bidflare.backend.dto.wishlist.WatchlistDTO;
import com.bidflare.backend.repository.UserRepository;
import com.bidflare.backend.service.WatchlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/wishlist")
@RequiredArgsConstructor
public class WatchlistController {

    private final WatchlistService wishlistService;
    @Autowired
    private UserRepository userRepository;

    private UUID getCurrentUserId(Principal principal) {
        return userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"))
                .getId();
    }

    @PreAuthorize("hasAnyRole('ADMIN','BUYER')")
    @GetMapping
    public ResponseEntity<List<WatchlistDTO.WishlistResponse>> getWishlist(Principal principal) {
        return ResponseEntity.ok(wishlistService.getWishlist(getCurrentUserId(principal)));
    }

    @PreAuthorize("hasAnyRole('ADMIN','BUYER')")
    @PostMapping
    public ResponseEntity<WatchlistDTO.WishlistResponse> add(@RequestBody WatchlistDTO.WishlistCreateRequest request, Principal principal) {
        return ResponseEntity.ok(wishlistService.addToWishlist(getCurrentUserId(principal), request));
    }

    @PreAuthorize("hasAnyRole('ADMIN','BUYER')")
    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> remove(@PathVariable UUID productId, Principal principal) {
        wishlistService.removeFromWishlist(getCurrentUserId(principal), productId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN','BUYER')")
    @GetMapping("/contains/{productId}")
    public ResponseEntity<Boolean> contains(@PathVariable UUID productId, Principal principal) {
        return ResponseEntity.ok(wishlistService.isWishlisted(getCurrentUserId(principal), productId));
    }

    @PreAuthorize("hasAnyRole('ADMIN','BUYER')")
    @GetMapping("/count")
    public ResponseEntity<Long> count(Principal principal) {
        return ResponseEntity.ok(wishlistService.getWishlistCount(getCurrentUserId(principal)));
    }
}
