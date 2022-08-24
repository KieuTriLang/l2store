package com.ktl.l2store.exception.User;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.ktl.l2store.exception.ApiException;

@ControllerAdvice
public class UserExceptionHanlder {
    @ExceptionHandler(value = { UserNotfoundException.class })
    public ResponseEntity<Object> userNotfoundException(UserNotfoundException exception) {
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        ApiException apiException = new ApiException(
                badRequest,
                exception.getMessage(),
                ZonedDateTime.now(ZoneId.of("Z")));
        return new ResponseEntity<>(apiException, badRequest);
    }
}
