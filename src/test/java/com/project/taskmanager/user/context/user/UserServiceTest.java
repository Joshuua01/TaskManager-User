package com.project.taskmanager.user.context.user;

import com.project.taskmanager.user.configuration.security.JwtService;
import com.project.taskmanager.user.context.user.dto.UserRequest;
import com.project.taskmanager.user.context.user.dto.UserResponse;
import com.project.taskmanager.user.domain.Role;
import com.project.taskmanager.user.domain.User;
import com.project.taskmanager.user.infrastructure.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserService(passwordEncoder, userRepository, jwtService);
    }

    @Test
    public void testCreateUser() {
        // Given
        UserRequest request = new UserRequest("John", "Doe", "john.doe@example.com", "password", Role.USER);
        when(userRepository.save(any(User.class))).thenReturn(User.builder().id(UUID.randomUUID()).build());
        when(passwordEncoder.encode(any(String.class))).thenReturn("encodedPassword");
        when(jwtService.generateToken(any(User.class))).thenReturn("token");

        // When
        UserResponse response = userService.createUser(request);

        // Then
        assertThat(response.getId()).isNotNull();
    }

    @Test
    public void testGetUserById() {
        var user = User.builder()
                .id(UUID.randomUUID())
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("password")
                .build();
        when(userRepository.findById(any(UUID.class))).thenReturn(java.util.Optional.of(user));
        var response = userService.getUserById(user.getId());

        assertThat(response.getId()).isEqualTo(user.getId());
    }
}