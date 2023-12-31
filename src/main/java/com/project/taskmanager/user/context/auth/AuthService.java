package com.project.taskmanager.user.context.auth;

import com.project.taskmanager.user.configuration.security.JwtService;
import com.project.taskmanager.user.context.auth.dto.AuthenticationRequest;
import com.project.taskmanager.user.context.auth.dto.AuthenticationResponse;
import com.project.taskmanager.user.context.auth.dto.RegisterRequest;
import com.project.taskmanager.user.domain.Role;
import com.project.taskmanager.user.domain.User;
import com.project.taskmanager.user.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        user.setLastToken(jwtToken);
        userRepository.save(user);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        var jwtToken = jwtService.generateToken(user);
        user.setLastToken(jwtToken);
        userRepository.save(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public void logout() {
        var user = userRepository.findById(UUID.fromString(SecurityContextHolder.getContext().getAuthentication().getName()))
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setLastToken(null);
        userRepository.save(user);
    }

    public boolean isValidToken(String token) {
        User user = userRepository.findById(UUID.fromString(jwtService.extractId(token)))
                .orElseThrow(() -> new RuntimeException("User not found"));
        return jwtService.isTokenValid(token, user);
    }
}
