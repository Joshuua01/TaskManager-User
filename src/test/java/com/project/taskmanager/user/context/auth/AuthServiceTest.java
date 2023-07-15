package com.project.taskmanager.user.context.auth;

import com.project.taskmanager.user.configuration.security.JwtService;
import com.project.taskmanager.user.context.auth.dto.AuthenticationRequest;
import com.project.taskmanager.user.context.auth.dto.AuthenticationResponse;
import com.project.taskmanager.user.context.auth.dto.RegisterRequest;
import com.project.taskmanager.user.domain.User;
import com.project.taskmanager.user.infrastructure.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthService authService;

    @Mock
    private AuthenticationManager authenticationManager;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        authService = new AuthService(userRepository, passwordEncoder, jwtService, authenticationManager);
    }

    @Test
    void testRegisterWithValidUserExpectTokenToBeNotNull() {
        RegisterRequest request = new RegisterRequest("John", "Doe", "john.doe@example.com", "password");
        when(userRepository.save(any(User.class))).thenReturn(User.builder().id(UUID.randomUUID()).build());
        when(passwordEncoder.encode(any(String.class))).thenReturn("encodedPassword");
        when(jwtService.generateToken(any(User.class))).thenReturn("token");
        AuthenticationResponse response = authService.register(request);

        assertThat(response.getToken()).isNotNull();
    }

    @Test
    void testAuthenticateWithValidUserExpectTokenToBeNotNull() {
        AuthenticationRequest request = new AuthenticationRequest("john.doe@example.com", "password");
        when(userRepository.save(any(User.class))).thenReturn(User.builder().id(UUID.randomUUID()).build());
        when(userRepository.findByEmail(any(String.class))).thenReturn(java.util.Optional.of(User.builder().id(UUID.randomUUID()).build()));
        when(passwordEncoder.encode(any(String.class))).thenReturn("encodedPassword");
        when(jwtService.generateToken(any(User.class))).thenReturn("token");
        AuthenticationResponse response = authService.authenticate(request);

        assertThat(response.getToken()).isNotNull();
    }
}