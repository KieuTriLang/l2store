package com.ktl.l2store.exception;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;

import lombok.Data;

@Data
public class ApiException {
    private HttpStatus httpStatus;
    private List<String> messages;
    private ZonedDateTime timestamp;

    public ApiException(HttpStatus httpStatus, String message, ZonedDateTime timestamp) {
        this.httpStatus = httpStatus;
        this.messages = new ArrayList<>();
        this.messages.add(message);
        this.timestamp = timestamp;
    }

    public ApiException(HttpStatus httpStatus, List<String> messages, ZonedDateTime timestamp) {
        this.httpStatus = httpStatus;
        this.messages = messages;
        this.timestamp = timestamp;
    }
}
