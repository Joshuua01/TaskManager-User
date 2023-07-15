package com.project.taskmanager.user.context.user;

import com.project.taskmanager.user.configuration.security.JwtService;
import com.project.taskmanager.user.context.user.dto.ChangePasswordRequest;
import com.project.taskmanager.user.context.user.dto.UserRequest;
import com.project.taskmanager.user.context.user.dto.UserResponse;
import com.project.taskmanager.user.context.user.dto.UserUpdateRequest;
import com.project.taskmanager.user.domain.Role;
import com.project.taskmanager.user.domain.User;
import com.project.taskmanager.user.infrastructure.UserRepository;
import com.project.taskmanager.user.tools.UserTestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
    public void testCreateUserWithValidDataExpectIdIsNotNull() {
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
    public void testGetUserByIdWithExistingUserExpectUser() {
        var user = UserTestDataFactory.createValidUser();
        when(userRepository.findById(any(UUID.class))).thenReturn(java.util.Optional.of(user));
        var response = userService.getUserById(user.getId());

        assertThat(response.getId()).isEqualTo(user.getId());
    }

    @Test
    public void testGetAllUsersWithExistingUsersExpectUsers() {
        var user = UserTestDataFactory.createValidUser();
        var user2 = UserTestDataFactory.createValidUser();
        when(userRepository.findAll()).thenReturn(java.util.List.of(user, user2));
        var response = userService.getAllUsers();

        assertThat(response.get(0).getId()).isEqualTo(user.getId());
        assertThat(response.get(1).getId()).isEqualTo(user2.getId());
    }

    @Test
    public void testUpdateUserWithValidUserAndValidRequestExpectEditedUser() {
        var user = UserTestDataFactory.createValidUser();
        UserUpdateRequest request = new UserUpdateRequest("John", "Doe", "john@doe.com", "password");
        when(userRepository.findById(any(UUID.class))).thenReturn(java.util.Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(passwordEncoder.encode(any(String.class))).thenReturn("encodedPassword");
        userService.updateUser(user.getId(), request);
        assertThat(user.getFirstName()).isEqualTo(request.getFirstName());
        assertThat(user.getLastName()).isEqualTo(request.getLastName());
        assertThat(user.getEmail()).isEqualTo(request.getEmail());
        assertThat(user.getPassword()).isEqualTo("encodedPassword");
    }

    @Test
    public void testDeleteUserWithUserInRepositoryExpectUserDeletion() {
        var user = UserTestDataFactory.createValidUser();
        when(userRepository.findById(any(UUID.class))).thenReturn(java.util.Optional.of(user));
        userService.deleteUser(user.getId());

        verify(userRepository, times(1)).delete(user);
    }

    @Test
    public void testChangePasswordWithValidUserAndValidRequestExpectPasswordChange() {
        var user = UserTestDataFactory.createValidUser();
        ChangePasswordRequest request = new ChangePasswordRequest("password", "newPassword");
        when(userRepository.findById(any(UUID.class))).thenReturn(java.util.Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(passwordEncoder.encode(any(String.class))).thenReturn("encodedPassword");
        when(passwordEncoder.matches(any(String.class), any(String.class))).thenReturn(true, false);

        userService.changePassword(user.getId(), request);

        assertThat(user.getPassword()).isEqualTo("encodedPassword");
    }
}
