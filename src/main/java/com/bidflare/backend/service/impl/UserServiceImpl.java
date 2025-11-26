package com.bidflare.backend.service.impl;

import com.bidflare.backend.dto.CreateUserRequestDto;
import com.bidflare.backend.dto.UpdateUserRequestDto;
import com.bidflare.backend.dto.UserDto;
import com.bidflare.backend.entity.User;
import com.bidflare.backend.event.NotificationEvent;
import com.bidflare.backend.mapper.UserMapper;
import com.bidflare.backend.repository.UserRepository;
import com.bidflare.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
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
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public UserDto createUser(CreateUserRequestDto request) {
        User user = User.builder()
                .email(request.email())
                .passwordHash(passwordEncoder.encode(request.password()))
                .name(request.name())
                .role(request.role())
                .isVerified(false)
                .build();

        User savedUser = userRepository.save(user);
        eventPublisher.publishEvent(new NotificationEvent(
                user.getId(),
                "USER_CREATED",
                "Your account was created successfully!",
                user.getId()
        ));
        return UserMapper.toDto(savedUser);
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
    public UserDto getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(UserMapper::toDto)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public UserDto updateUser(UUID id, UpdateUserRequestDto request) {
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

        eventPublisher.publishEvent(new NotificationEvent(
                updatedUser.getId(),
                "USER_UPDATED",
                "Your account was updated successfully!",
                updatedUser.getId()
        ));

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
