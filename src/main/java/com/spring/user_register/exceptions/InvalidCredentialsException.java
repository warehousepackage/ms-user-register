package com.spring.user_register.exceptions;

public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException(){ super("Invalid credentials"); }
}
