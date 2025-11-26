package com.bidflare.backend.service.impl;

import com.bidflare.backend.config.jwt.JwtUtil;
import com.bidflare.backend.dto.AuthResponseDto;
import com.bidflare.backend.dto.LoginRequestDto;
import com.bidflare.backend.dto.UserDto;
import com.bidflare.backend.service.AuthService;
import com.bidflare.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final UserService userService;

    @Override
    public AuthResponseDto login(LoginRequestDto request) {
        Authentication auth = new UsernamePasswordAuthenticationToken(
                request.getEmail(), request.getPassword());
        authManager.authenticate(auth);

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        UserDto user = userService.getUserByEmail(request.getEmail());

        String token = jwtUtil.generateToken(userDetails.getUsername(), user.id().toString(), user.role().name());

        return new AuthResponseDto(token);
    }
}
