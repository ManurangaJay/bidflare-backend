package com.bidflare.backend.controller;

import com.bidflare.backend.config.jwt.JwtUtil;
import com.bidflare.backend.dto.address.AddressDto;
import com.bidflare.backend.service.AddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;
    private final JwtUtil jwtUtil;

    @PostMapping
    public ResponseEntity<AddressDto.AddressResponse> createAddress(@Valid @RequestBody AddressDto.AddressRequest request) {
        UUID userId = getCurrentUserId();
        AddressDto.AddressResponse createdAddress = addressService.createAddress(request, userId);
        return new ResponseEntity<>(createdAddress, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<AddressDto.AddressResponse>> getUserAddresses() {
        UUID userId = getCurrentUserId();
        List<AddressDto.AddressResponse> addresses = addressService.getAddressesByUserId(userId);
        return ResponseEntity.ok(addresses);
    }

    @PutMapping("/{addressId}")
    public ResponseEntity<AddressDto.AddressResponse> updateAddress(@PathVariable UUID addressId, @Valid @RequestBody AddressDto.AddressRequest request) {
        UUID userId = getCurrentUserId();
        AddressDto.AddressResponse updatedAddress = addressService.updateAddress(addressId, request, userId);
        return ResponseEntity.ok(updatedAddress);
    }

    @PatchMapping("/{addressId}/set-default")
    public ResponseEntity<AddressDto.AddressResponse> setDefaultAddress(@PathVariable UUID addressId) {
        UUID userId = getCurrentUserId();
        AddressDto.AddressResponse defaultAddress = addressService.setDefaultAddress(addressId, userId);
        return ResponseEntity.ok(defaultAddress);
    }

    @DeleteMapping("/{addressId}")
    public ResponseEntity<Void> deleteAddress(@PathVariable UUID addressId) {
        UUID userId = getCurrentUserId();
        addressService.deleteAddress(addressId, userId);
        return ResponseEntity.noContent().build();
    }

    private UUID getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userIdString = jwtUtil.extractUserIdFromAuthentication(authentication);
        return UUID.fromString(userIdString);
    }
}