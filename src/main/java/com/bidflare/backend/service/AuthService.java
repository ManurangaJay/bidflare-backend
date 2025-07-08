package com.bidflare.backend.service;

import com.bidflare.backend.dto.AuthResponseDto;
import com.bidflare.backend.dto.LoginRequestDto;

public interface AuthService {
    AuthResponseDto login(LoginRequestDto request);
}
