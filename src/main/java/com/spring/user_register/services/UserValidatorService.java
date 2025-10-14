package com.spring.user_register.services;

import com.spring.user_register.dto.request.RegisterRequestDTO;
import com.spring.user_register.exceptions.InvalidEmailAndPasswordException;
import com.spring.user_register.exceptions.InvalidEmailException;
import com.spring.user_register.exceptions.InvalidPasswordException;

public class UserValidatorService {

    public class constants {
        public static final int MAX_PASSWORD_LENGTH = 45;
    }

    public static void validateRegisterData(RegisterRequestDTO dto) {
        validateEmailAndPassword(dto.email(), dto.password());
        validateEmail(dto.email());
        validatePassword(dto.password());
    }

    private static void validateEmail(String email) {
        if (email == null || !email.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            throw new InvalidEmailException();
        }
    }

    private static void validatePassword(String password) {
        if (password == null || password.length() > constants.MAX_PASSWORD_LENGTH) {
            throw new InvalidPasswordException();
        }
    }

    private static void validateEmailAndPassword(String email, String password) {
        if ((email == null || email.isBlank()) && (password == null || password.isBlank())) {
            throw new InvalidEmailAndPasswordException();
        }
    }
}
