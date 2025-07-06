package com.bidflare.backend.service;

import com.bidflare.backend.dto.CreateUserRequest;
import com.bidflare.backend.dto.UserDto;

import java.util.List;
import java.util.UUID;

public interface UserService {
    UserDto createUser(CreateUserRequest request);
    List<UserDto> getAllUsers();
    UserDto getUserById(UUID id);
    UserDto updateUser(UUID id, UserDto userDto);
    void deleteUser(UUID id);
}
