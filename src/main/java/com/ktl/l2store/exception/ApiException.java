package com.ktl.l2store.exception;

import java.time.ZonedDateTime;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ApiException {
    private HttpStatus httpStatus;
    private String message;
    private ZonedDateTime timestamp;
}
