package com.bidflare.backend.controller;

import com.bidflare.backend.dto.*;
import com.bidflare.backend.entity.User;
import com.bidflare.backend.security.jwt.JwtUtil;
import com.bidflare.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        Authentication auth = new UsernamePasswordAuthenticationToken(
                request.getEmail(), request.getPassword());

        authManager.authenticate(auth);

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());

        // Get the full User entity to access userId
        UserDto user = userService.getUserByEmail(request.getEmail());

        String token = jwtUtil.generateToken(userDetails.getUsername(), user.id().toString(),user.role().name());

        return ResponseEntity.ok(new AuthResponse(token));
    }
}
