package edu.spring.module;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRequestDto(
        String email,
        @Size(min = 6) @NotBlank String password,
        String confirmPassword
) {}
