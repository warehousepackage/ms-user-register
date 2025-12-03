package com.spring.user_register.service;

import com.spring.user_register.dto.request.RegisterRequestDTO;
import com.spring.user_register.exceptions.InvalidEmailAndPasswordException;
import com.spring.user_register.exceptions.InvalidEmailException;
import com.spring.user_register.exceptions.InvalidPasswordException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes Unitários para UserValidatorService")
public class UserValidatorServiceTest {

    // --- Testes para validateRegisterData ---

    @Test
    @DisplayName("Deve validar os dados com sucesso quando email e password forem válidos")
    void validateRegisterData_ShouldValidateSuccessfully() {
        RegisterRequestDTO dto = new RegisterRequestDTO(
                "Erick",
                "email@teste.com",
                "senha123",
                "USER"
        );

        assertDoesNotThrow(() -> UserValidatorService.validateRegisterData(dto));
    }

    // --- Testes para validateEmailAndPassword ---

    @Test
    @DisplayName("Deve lançar InvalidEmailAndPasswordException quando email e password forem nulos ou vazios")
    void validateRegisterData_ShouldThrowInvalidEmailAndPasswordException_WhenEmailAndPasswordAreEmptyOrNull() {
        RegisterRequestDTO dto = new RegisterRequestDTO(
                "Nome",
                null,
                null,
                "USER"
        );

        assertThrows(InvalidEmailAndPasswordException.class,
                () -> UserValidatorService.validateRegisterData(dto));
    }

    @Test
    @DisplayName("Deve lançar InvalidEmailAndPasswordException quando email e password forem strings vazias")
    void validateRegisterData_ShouldThrowInvalidEmailAndPasswordException_WhenEmailAndPasswordAreBlank() {
        RegisterRequestDTO dto = new RegisterRequestDTO(
                "Nome",
                "",
                "",
                "USER"
        );

        assertThrows(InvalidEmailAndPasswordException.class,
                () -> UserValidatorService.validateRegisterData(dto));
    }

    // --- Testes para validateEmail ---

    @Test
    @DisplayName("Deve lançar InvalidEmailException quando email for nulo")
    void validateEmail_ShouldThrowInvalidEmailException_WhenEmailIsNull() {
        RegisterRequestDTO dto = new RegisterRequestDTO(
                "Nome",
                null,
                "senha123",
                "USER"
        );

        assertThrows(InvalidEmailException.class,
                () -> UserValidatorService.validateRegisterData(dto));
    }

    @Test
    @DisplayName("Deve lançar InvalidEmailException quando email tiver formato inválido")
    void validateEmail_ShouldThrowInvalidEmailException_WhenEmailHasInvalidFormat() {
        RegisterRequestDTO dto = new RegisterRequestDTO(
                "Nome",
                "email-invalido",
                "senha123",
                "USER"
        );

        assertThrows(InvalidEmailException.class,
                () -> UserValidatorService.validateRegisterData(dto));
    }

    // --- Testes para validatePassword ---

    @Test
    @DisplayName("Deve lançar InvalidPasswordException quando password for nulo")
    void validatePassword_ShouldThrowInvalidPasswordException_WhenPasswordIsNull() {
        RegisterRequestDTO dto = new RegisterRequestDTO(
                "Nome",
                "email@teste.com",
                null,
                "USER"
        );

        assertThrows(InvalidPasswordException.class,
                () -> UserValidatorService.validateRegisterData(dto));
    }

    @Test
    @DisplayName("Deve lançar InvalidPasswordException quando password exceder 45 caracteres")
    void validatePassword_ShouldThrowInvalidPasswordException_WhenPasswordExceedsMaxLength() {
        String longPassword = "a".repeat(50);

        RegisterRequestDTO dto = new RegisterRequestDTO(
                "Nome",
                "email@teste.com",
                longPassword,
                "USER"
        );

        assertThrows(InvalidPasswordException.class,
                () -> UserValidatorService.validateRegisterData(dto));
    }

    @Test
    @DisplayName("Deve validar corretamente quando password é exatamente 45 caracteres")
    void validatePassword_ShouldAcceptPassword_WhenPasswordHas45Chars() {
        String password45 = "a".repeat(45);

        RegisterRequestDTO dto = new RegisterRequestDTO(
                "Nome",
                "email@teste.com",
                password45,
                "USER"
        );

        assertDoesNotThrow(() -> UserValidatorService.validateRegisterData(dto));
    }
}
