package com.bidflare.backend.mapper;

import com.bidflare.backend.dto.UserDto;
import com.bidflare.backend.entity.User;

public class UserMapper {

    public static UserDto toDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .role(user.getRole())
                .isVerified(user.isVerified())
                .profileImage(user.getProfileImage())
                .build();
    }
}
