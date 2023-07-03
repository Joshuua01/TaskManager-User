package com.project.task.user.context.user;

import com.project.task.user.context.user.dto.UserDetailsResponse;
import com.project.task.user.context.user.dto.UserRequest;
import com.project.task.user.context.user.dto.UserResponse;
import com.project.task.user.domain.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserEndpoint {

    private final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<UserResponse> createUser(@RequestBody UserRequest request) {
        return ResponseEntity.ok(userService.createUser(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDetailsResponse> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }


}
