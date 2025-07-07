package com.bidflare.backend.service.impl;

import com.bidflare.backend.dto.CreateUserRequest;
import com.bidflare.backend.dto.UpdateUserRequest;
import com.bidflare.backend.dto.UserDto;
import com.bidflare.backend.entity.User;
import com.bidflare.backend.mapper.UserMapper;
import com.bidflare.backend.repository.UserRepository;
import com.bidflare.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.bidflare.backend.exception.ResourceNotFoundException;


import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDto createUser(CreateUserRequest request) {
        User user = User.builder()
                .email(request.email())
                .passwordHash(passwordEncoder.encode(request.password()))
                .name(request.name())
                .role(request.role())
                .isVerified(false)
                .build();
        return UserMapper.toDto(userRepository.save(user));
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserMapper::toDto)
                .toList();
    }

    @Override
    public UserDto getUserById(UUID id) {
        return userRepository.findById(id)
                .map(UserMapper::toDto)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public UserDto updateUser(UUID id, UpdateUserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        boolean updated = false;

        if (request.name() != null && !request.name().equals(user.getName())) {
            user.setName(request.name());
            updated = true;
        }

        if (request.email() != null && !request.email().equals(user.getEmail())) {
            user.setEmail(request.email());
            updated = true;
        }

        if (request.password() != null && !request.password().isBlank()) {
            user.setPasswordHash(passwordEncoder.encode(request.password()));
            updated = true;
        }

        if (request.profileImage() != null && !request.profileImage().equals(user.getProfileImage())) {
            user.setProfileImage(request.profileImage());
            updated = true;
        }

        if (!updated) {
            throw new IllegalArgumentException("No fields provided or no changes detected.");
        }

        User updatedUser = userRepository.save(user);
        return UserMapper.toDto(updatedUser);
    }




    @Override
    public void deleteUser(UUID id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found");
        }
        userRepository.deleteById(id);
    }
}
