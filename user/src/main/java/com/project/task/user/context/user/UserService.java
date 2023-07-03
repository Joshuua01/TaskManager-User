package com.project.task.user.context.user;

import com.project.task.user.configuration.security.JwtService;
import com.project.task.user.context.auth.dto.AuthenticationResponse;
import com.project.task.user.context.user.dto.UserDetailsResponse;
import com.project.task.user.context.user.dto.UserRequest;
import com.project.task.user.context.user.dto.UserResponse;
import com.project.task.user.domain.Role;
import com.project.task.user.domain.User;
import com.project.task.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
        userRepository.save(user);

        return UserResponse.builder()
                .id(user.getId())
                .build();
    }

    public UserDetailsResponse getUserById(Long id) {
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
}
