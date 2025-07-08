package com.bidflare.backend.service;

import com.bidflare.backend.dto.CreateUserRequestDto;
import com.bidflare.backend.dto.UpdateUserRequestDto;
import com.bidflare.backend.dto.UserDto;

import java.util.List;
import java.util.UUID;

public interface UserService {
    UserDto createUser(CreateUserRequestDto request);
    List<UserDto> getAllUsers();
    UserDto getUserById(UUID id);
    UserDto updateUser(UUID id, UpdateUserRequestDto request);
    void deleteUser(UUID id);
    UserDto getUserByEmail(String email);
}
