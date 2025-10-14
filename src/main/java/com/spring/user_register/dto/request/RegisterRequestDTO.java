package com.spring.user_register.dto.request;

public record RegisterRequestDTO(
        String name,
        String email,
        String password
) {}
