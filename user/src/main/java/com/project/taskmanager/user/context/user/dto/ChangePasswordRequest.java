package com.project.taskmanager.user.context.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordRequest {
    @NotBlank(message = "Old password can not be empty")
    private String oldPassword;
    @NotBlank(message = "New password can not be empty")
    private String newPassword;
}
