package com.bidflare.backend.dto;

import lombok.Data;

@Data
public class LoginRequestDto {
    private String email;
    private String password;
}
