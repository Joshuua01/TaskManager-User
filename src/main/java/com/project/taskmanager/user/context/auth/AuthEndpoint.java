package com.project.taskmanager.user.context.auth;

import com.project.taskmanager.user.context.auth.dto.AuthenticationRequest;
import com.project.taskmanager.user.context.auth.dto.AuthenticationResponse;
import com.project.taskmanager.user.context.auth.dto.RegisterRequest;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthEndpoint {
    @Value("${secrets.internal}")
    private String InternalSecret;
    private final AuthService authService;

    @Operation(summary = "Register a new user", description = "Returns the JWT token for the created user")
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @Operation(summary = "Authenticate a user", description = "Returns the JWT token for the authenticated user")
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authService.authenticate(request));
    }

    @Operation(summary = "Logout a user", description = "Returns a message")
    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        authService.logout();
        return ResponseEntity.ok("User logged out!");
    }

    @Operation(summary = "INTERNAL ENDPOINT - Check if a token is valid", description = "Returns a boolean")
    @GetMapping("/internal/isValidToken/{token}")
    public ResponseEntity<Boolean> isValidToken(@PathVariable String token, @RequestHeader("AuthInt") String authorization) {
        if (!authorization.equals(InternalSecret)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(authService.isValidToken(token));
    }
}
