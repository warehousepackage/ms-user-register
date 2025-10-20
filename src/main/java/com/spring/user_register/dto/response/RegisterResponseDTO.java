package com.spring.user_register.dto.response;

public record RegisterResponseDTO(
        String name,
        String email,
        String password,
        String role
) {}
