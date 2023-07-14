package com.project.taskmanager.user.context.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest {
    @NotBlank(message = "Email can not be empty")
    private String email;
    @NotBlank(message = "Password can not be empty")
    private String password;
}
