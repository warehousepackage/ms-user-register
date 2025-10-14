package com.spring.user_register.exceptions;

public class InvalidEmailAndPasswordException extends RuntimeException {
    public InvalidEmailAndPasswordException() { super("The email and password fields are mandatory."); }
}
