package com.spring.user_register.exceptions;

import com.auth0.jwt.exceptions.JWTCreationException;

public class AuthenticationErrorException extends RuntimeException {
    public AuthenticationErrorException(JWTCreationException exception) {
        super("Error while authenticating " + exception.getMessage());
    }
}
