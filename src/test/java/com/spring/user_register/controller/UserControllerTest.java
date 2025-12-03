package com.spring.user_register.controller;

import com.spring.user_register.controllers.UserController;
import com.spring.user_register.dto.request.LoginRequestDTO;
import com.spring.user_register.dto.request.RegisterRequestDTO;
import com.spring.user_register.dto.response.LoginResponseDTO;
import com.spring.user_register.dto.response.RegisterResponseDTO;
import com.spring.user_register.entities.User;
import com.spring.user_register.enums.RolesEnum;
import com.spring.user_register.exceptions.EmailAlreadyExistsException;
import com.spring.user_register.exceptions.InvalidCredentialsException;
import com.spring.user_register.exceptions.UserNotFoundException;
import com.spring.user_register.repository.UserRepository;
import com.spring.user_register.security.TokenService;
import com.spring.user_register.service.UserValidatorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes Unitários para UserController")
public class UserControllerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private TokenService tokenService;

    @InjectMocks
    private UserController userController;

    private User user;
    private LoginRequestDTO loginRequest;
    private RegisterRequestDTO registerRequest;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(UUID.randomUUID());
        user.setName("Erick");
        user.setEmail("email@teste.com");
        user.setPassword("encodedPwd");
        user.setRole(RolesEnum.RH);

        loginRequest = new LoginRequestDTO(
                "email@teste.com",
                "senha123"
        );

        registerRequest = new RegisterRequestDTO(
                "Erick",
                "email@teste.com",
                "senha123",
                "RH"
        );
    }

    // -------------------------------------------------------------
    // LOGIN
    // -------------------------------------------------------------

    @Test
    @DisplayName("Deve realizar login com sucesso")
    void login_ShouldLoginSuccessfully() {
        when(userRepository.findByEmail(loginRequest.email())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(loginRequest.password(), user.getPassword())).thenReturn(true);
        when(tokenService.generateToken(user)).thenReturn("mocked-token");

        ResponseEntity response = userController.login(loginRequest);

        assertNotNull(response.getBody());
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("mocked-token", ((LoginResponseDTO) response.getBody()).token());

        verify(userRepository, times(1)).findByEmail(loginRequest.email());
        verify(passwordEncoder, times(1)).matches(loginRequest.password(), user.getPassword());
        verify(tokenService, times(1)).generateToken(user);
    }

    @Test
    @DisplayName("Deve lançar UserNotFoundException quando email não existir")
    void login_ShouldThrowUserNotFoundException_WhenEmailNotFound() {
        when(userRepository.findByEmail(loginRequest.email())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userController.login(loginRequest));

        verify(userRepository, times(1)).findByEmail(loginRequest.email());
        verify(passwordEncoder, never()).matches(anyString(), anyString());
        verify(tokenService, never()).generateToken(any());
    }

    @Test
    @DisplayName("Deve lançar InvalidCredentialsException quando a senha estiver incorreta")
    void login_ShouldThrowInvalidCredentialsException_WhenPasswordDoesNotMatch() {
        when(userRepository.findByEmail(loginRequest.email())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(loginRequest.password(), user.getPassword())).thenReturn(false);

        assertThrows(InvalidCredentialsException.class, () -> userController.login(loginRequest));

        verify(passwordEncoder, times(1)).matches(loginRequest.password(), user.getPassword());
        verify(tokenService, never()).generateToken(any());
    }

    // -------------------------------------------------------------
    // REGISTER
    // -------------------------------------------------------------

    @Test
    @DisplayName("Deve registrar usuário com sucesso")
    void register_ShouldRegisterSuccessfully() {
        when(userRepository.findByEmail(registerRequest.email())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(registerRequest.password())).thenReturn("encodedPwd");
        when(userRepository.save(any(User.class))).thenReturn(user);

        ResponseEntity<RegisterResponseDTO> response = userController.register(registerRequest);

        assertEquals(201, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(registerRequest.email(), response.getBody().email());
        assertEquals(registerRequest.name(), response.getBody().name());
        assertEquals(registerRequest.password(), response.getBody().password());

        verify(userRepository, times(1)).findByEmail(registerRequest.email());
        verify(passwordEncoder, times(1)).encode(registerRequest.password());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Deve lançar EmailAlreadyExistsException quando email já estiver cadastrado")
    void register_ShouldThrowEmailAlreadyExistsException_WhenEmailAlreadyExists() {
        when(userRepository.findByEmail(registerRequest.email())).thenReturn(Optional.of(user));

        assertThrows(EmailAlreadyExistsException.class, () -> userController.register(registerRequest));

        verify(userRepository, times(1)).findByEmail(registerRequest.email());
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve chamar UserValidatorService.validateRegisterData() antes de registrar")
    void register_ShouldCallValidatorService() {
        try (var mocked = mockStatic(UserValidatorService.class)) {

            when(userRepository.findByEmail(registerRequest.email())).thenReturn(Optional.empty());
            when(passwordEncoder.encode(registerRequest.password())).thenReturn("encodedPwd");
            when(userRepository.save(any(User.class))).thenReturn(user);

            userController.register(registerRequest);

            mocked.verify(() -> UserValidatorService.validateRegisterData(registerRequest), times(1));
        }
    }
}