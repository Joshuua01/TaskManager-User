package com.project.taskmanager.user.context.user;

import com.project.taskmanager.user.configuration.security.JwtService;
import com.project.taskmanager.user.context.user.dto.*;
import com.project.taskmanager.user.domain.User;
import com.project.taskmanager.user.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public UserResponse createUser(UserRequest request) {
        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();
        user = userRepository.save(user);

        return UserResponse.builder()
                .id(user.getId())
                .build();
    }

    public UserDetailsResponse getUserById(UUID id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return UserDetailsResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();
    }

    public List<UserDetailsResponse> getAllUsers() {
        List<User> users = userRepository.findAll();

        List<UserDetailsResponse> userDetailsResponses = new ArrayList<>();
        for (User user : users) {
            UserDetailsResponse userDetailsResponse = UserDetailsResponse.builder()
                    .id(user.getId())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .email(user.getEmail())
                    .role(user.getRole().name())
                    .build();

            userDetailsResponses.add(userDetailsResponse);
        }
        return userDetailsResponses;
    }

    public UserDetailsResponse updateUser(UUID id, UserUpdateRequest request) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (request.getFirstName() != null) {
            user.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null) {
            user.setLastName(request.getLastName());
        }
        if (request.getEmail() != null) {
            user.setEmail(request.getEmail());
        }
        if (request.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        User updatedUser = userRepository.save(user);

        return UserDetailsResponse.builder()
                .id(updatedUser.getId())
                .firstName(updatedUser.getFirstName())
                .lastName(updatedUser.getLastName())
                .email(updatedUser.getEmail())
                .role(updatedUser.getRole().name())
                .build();
    }

    public void deleteUser(UUID id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.delete(user);
    }

    public void changePassword(UUID id, ChangePasswordRequest request) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new RuntimeException("Old password is incorrect");
        }
        if (passwordEncoder.matches(request.getNewPassword(), user.getPassword())) {
            throw new RuntimeException("New password is the same as old password");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    public UserDetailsResponse getMe() {
        var userId = SecurityContextHolder.getContext().getAuthentication().getName();
        var user = userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new RuntimeException("User not found"));

        return UserDetailsResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();
    }

    public String getUserNameById(UUID id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user.getFirstName() + " " + user.getLastName();
    }
}
