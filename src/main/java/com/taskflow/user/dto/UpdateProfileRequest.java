package com.taskflow.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateProfileRequest(
    @NotBlank @Size(min = 2, max = 64) String username,
    @NotBlank @Email @Size(max = 255) String email
) {}
