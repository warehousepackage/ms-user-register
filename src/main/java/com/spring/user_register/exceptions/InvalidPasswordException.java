package com.spring.user_register.exceptions;

public class InvalidPasswordException extends RuntimeException {
    public InvalidPasswordException() { super("The password must have a maximum of 45 characters and is mandatory"); }
}
