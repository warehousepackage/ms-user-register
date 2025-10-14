package com.spring.user_register.exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() { super("User not found"); }

    public UserNotFoundException(String message) { super(message); }

    public static UserNotFoundException withEmail(String email) {
        return new UserNotFoundException("User not found with email: " + email);
    }
}
