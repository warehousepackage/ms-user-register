package com.spring.user_register.exceptions;

public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException() { super("Email is already in use"); }
}
