package com.spring.user_register.controllers;

import com.spring.user_register.dto.response.RegisterResponseDTO;
import com.spring.user_register.dto.response.LoginResponseDTO;
import com.spring.user_register.dto.request.LoginRequestDTO;
import com.spring.user_register.dto.request.RegisterRequestDTO;
import com.spring.user_register.entities.User;
import com.spring.user_register.enums.RolesEnum;
import com.spring.user_register.exceptions.EmailAlreadyExistsException;
import com.spring.user_register.exceptions.InvalidCredentialsException;
import com.spring.user_register.exceptions.UserNotFoundException;
import com.spring.user_register.repository.UserRepository;
import com.spring.user_register.security.TokenService;
import com.spring.user_register.services.UserValidatorService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class UserController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder, TokenService tokenService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginRequestDTO body){
        User user = this.userRepository.findByEmail(body.email())
                .orElseThrow(() -> UserNotFoundException.withEmail(body.email()));

        if (passwordEncoder.matches(body.password(), user.getPassword())){
            String token = this.tokenService.generateToken(user);
            return ResponseEntity.status(200).body(new LoginResponseDTO(token));
        }

        throw new InvalidCredentialsException();
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDTO> register(@RequestBody RegisterRequestDTO body){
        UserValidatorService.validateRegisterData(body);
        Optional<User> user = this.userRepository.findByEmail(body.email());

        if (user.isEmpty()){
            User newUser = new User();
            newUser.setName(body.name());
            newUser.setEmail(body.email());
            newUser.setPassword(passwordEncoder.encode(body.password()));
            newUser.setRole(RolesEnum.valueOf(body.role()));
            this.userRepository.save(newUser);

            RegisterResponseDTO response = new RegisterResponseDTO(
                    newUser.getName(),
                    newUser.getEmail(),
                    body.password(),
                    newUser.getRole().name()
            );

            return ResponseEntity.status(201).body(response);
        }

        throw new EmailAlreadyExistsException();
    }
}
