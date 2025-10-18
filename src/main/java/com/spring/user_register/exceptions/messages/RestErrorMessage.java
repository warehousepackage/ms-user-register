package com.spring.user_register.exceptions.messages;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class RestErrorMessage {
    private HttpStatus status;
    private String message;

    public RestErrorMessage() {
    }

    public RestErrorMessage(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
