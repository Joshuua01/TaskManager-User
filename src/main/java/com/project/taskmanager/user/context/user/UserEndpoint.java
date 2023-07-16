package com.project.taskmanager.user.context.user;

import com.project.taskmanager.user.context.user.dto.*;
import com.project.taskmanager.user.domain.Role;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class UserEndpoint {

    @Value("${secrets.internal}")
    private String InternalSecret;
    private final UserService userService;

    private boolean isAdmin() {
        return userService.getMe().getRole().equals(Role.ADMIN.name());
    }

    private boolean isTheSameUser(UUID id) {
        return userService.getMe().getId().equals(id);
    }

    @Operation(summary = "Create a new user", description = "Returns the created user")
    @PostMapping("/create")
    public ResponseEntity<UserResponse> createUser(@RequestBody UserRequest request) {
        if (isAdmin()) {
            return ResponseEntity.ok(userService.createUser(request));
        }
        throw new RuntimeException("User is not authorized");
    }

    @Operation(summary = "Get user by id", description = "Returns the user with the given id")
    @GetMapping("/{id}")
    public ResponseEntity<UserDetailsResponse> getUserById(@PathVariable UUID id) {
        if (isAdmin() || isTheSameUser(id)) {
            return ResponseEntity.ok(userService.getUserById(id));
        }
        throw new RuntimeException("User is not authorized");
    }

    @Operation(summary = "INTERNAL ENDPOINT - Get user by username", description = "Returns the user with the given username")
    @GetMapping("/internal/{id}")
    public ResponseEntity<String> getUserNameById(@PathVariable UUID id, @RequestHeader("AuthInt") String authorization) {
        if (authorization != null && authorization.equals(InternalSecret)) {
            return ResponseEntity.ok(userService.getUserNameById(id));
        }
        throw new RuntimeException("User is not authorized");
    }

    @Operation(summary = "Get all users", description = "Returns all users")
    @GetMapping("/all")
    public ResponseEntity<List<UserDetailsResponse>> getAllUsers() {
        if (isAdmin()) {
            return ResponseEntity.ok(userService.getAllUsers());
        }
        throw new RuntimeException("User is not authorized");
    }

    @Operation(summary = "Update user", description = "Returns the updated user")
    @PatchMapping("/update/{id}")
    public ResponseEntity<UserDetailsResponse> updateUser(@PathVariable UUID id, @RequestBody UserUpdateRequest request) {
        if (isAdmin() || isTheSameUser(id)) {
            return ResponseEntity.ok(userService.updateUser(id, request));
        }
        throw new RuntimeException("User is not authorized");
    }

    @Operation(summary = "Delete user", description = "Returns a message that the user was deleted successfully")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable UUID id) {
        if (isAdmin() || isTheSameUser(id)) {
            userService.deleteUser(id);
            return ResponseEntity.ok("User deleted successfully");
        }
        throw new RuntimeException("User is not authorized");
    }

    @Operation(summary = "Change password", description = "Returns a message that the password was changed successfully")
    @PatchMapping("/change-password/{id}")
    public ResponseEntity<String> changePassword(@PathVariable UUID id, @RequestBody ChangePasswordRequest request) {
        if (isAdmin() || isTheSameUser(id)) {
            userService.changePassword(id, request);
            return ResponseEntity.ok("Password changed successfully");
        }
        throw new RuntimeException("User is not authorized");
    }

    @Operation(summary = "Get current user", description = "Returns the current user")
    @GetMapping("/me")
    public ResponseEntity<UserDetailsResponse> getMe() {
        return ResponseEntity.ok(userService.getMe());
    }
}
