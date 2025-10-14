package com.spring.user_register.exceptions;

public class InvalidEmailException extends RuntimeException {
    public InvalidEmailException() { super("Invalid or required email address"); }
}
