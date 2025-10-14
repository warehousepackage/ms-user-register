package com.spring.user_register.controllers;

import com.spring.user_register.dto.ResponseDTO;
import com.spring.user_register.dto.request.LoginRequestDTO;
import com.spring.user_register.dto.request.RegisterRequestDTO;
import com.spring.user_register.entities.User;
import com.spring.user_register.exceptions.EmailAlreadyExistsException;
import com.spring.user_register.exceptions.InvalidCredentialsException;
import com.spring.user_register.exceptions.UserNotFoundException;
import com.spring.user_register.repository.UserRepository;
import com.spring.user_register.security.TokenService;
import com.spring.user_register.services.UserValidatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginRequestDTO body){
        User user = this.userRepository.findByEmail(body.email())
                .orElseThrow(() -> UserNotFoundException.withEmail(body.email()));

        if (passwordEncoder.matches(body.password(), user.getPassword())){
            String token = this.tokenService.generateToken(user);
            return ResponseEntity.status(200).body(new ResponseDTO(token));
        }

        throw new InvalidCredentialsException();
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody RegisterRequestDTO body){
        UserValidatorService.validateRegisterData(body);
        Optional<User> user = this.userRepository.findByEmail(body.email());

        if (user.isEmpty()){
            User newUser = new User();
            newUser.setEmail(body.email());
            newUser.setPassword(passwordEncoder.encode(body.password()));
            this.userRepository.save(newUser);

            String token = this.tokenService.generateToken(newUser);
            return ResponseEntity.status(201).body(new ResponseDTO(token));
        }

        throw new EmailAlreadyExistsException();
    }
}
