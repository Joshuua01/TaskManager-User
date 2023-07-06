package com.project.taskmanager.user.context.user;

import com.project.taskmanager.user.context.user.dto.*;
import com.project.taskmanager.user.domain.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserEndpoint {

    private final UserService userService;

    private boolean isAdmin() {
        return userService.getMe().getRole().equals(Role.ADMIN.name());
    }

    private boolean isTheSameUser(UUID id) {
        return userService.getMe().getId().equals(id);
    }

    @PostMapping("/create")
    public ResponseEntity<UserResponse> createUser(@RequestBody UserRequest request) {
        if (isAdmin()) {
            return ResponseEntity.ok(userService.createUser(request));
        }
        throw new RuntimeException("User is not authorized");
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDetailsResponse> getUserById(@PathVariable UUID id) {
        if (isAdmin() || isTheSameUser(id)) {
            return ResponseEntity.ok(userService.getUserById(id));
        }
        throw new RuntimeException("User is not authorized");
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserDetailsResponse>> getAllUsers() {
        if (isAdmin()) {
            return ResponseEntity.ok(userService.getAllUsers());
        }
        throw new RuntimeException("User is not authorized");
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<UserDetailsResponse> updateUser(@PathVariable UUID id, @RequestBody UserUpdateRequest request) {
        if (isAdmin() || isTheSameUser(id)) {
            return ResponseEntity.ok(userService.updateUser(id, request));
        }
        throw new RuntimeException("User is not authorized");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable UUID id) {
        if (isAdmin() || isTheSameUser(id)) {
            userService.deleteUser(id);
            return ResponseEntity.ok("User deleted successfully");
        }
        throw new RuntimeException("User is not authorized");
    }

    @PatchMapping("/change-password/{id}")
    public ResponseEntity<String> changePassword(@PathVariable UUID id, @RequestBody ChangePasswordRequest request) {
        if (isAdmin() || isTheSameUser(id)) {
            userService.changePassword(id, request);
            return ResponseEntity.ok("Password changed successfully");
        }
        throw new RuntimeException("User is not authorized");
    }

    @GetMapping("/me")
    public ResponseEntity<UserDetailsResponse> getMe() {
        return ResponseEntity.ok(userService.getMe());
    }
}
