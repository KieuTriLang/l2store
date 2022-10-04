package com.ktl.l2store.exception;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ApiExceptionHanlder {
    @ExceptionHandler(value = { ItemNotfoundException.class })
    public ResponseEntity<Object> itemNotfoundException(ItemNotfoundException exception) {
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        ApiException apiException = new ApiException(
                badRequest,
                exception.getMessage(),
                ZonedDateTime.now(ZoneId.of("Z")));
        return new ResponseEntity<>(apiException, badRequest);
    }

    @ExceptionHandler(value = { ItemExistException.class })
    public ResponseEntity<Object> itemExistException(ItemExistException exception) {
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        ApiException apiException = new ApiException(
                badRequest,
                exception.getMessage(),
                ZonedDateTime.now(ZoneId.of("Z")));
        return new ResponseEntity<>(apiException, badRequest);
    }

    @ExceptionHandler(value = { IOException.class })
    public ResponseEntity<Object> ioException(IOException exception) {
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        ApiException apiException = new ApiException(
                badRequest,
                exception.getMessage(),
                ZonedDateTime.now(ZoneId.of("Z")));
        return new ResponseEntity<>(apiException, badRequest);
    }

    @ExceptionHandler(value = { ListException.class })
    public ResponseEntity<Object> listException(ListException exception) {
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        ApiException apiException = new ApiException(
                badRequest,
                exception.getMessages(),
                ZonedDateTime.now(ZoneId.of("Z")));
        return new ResponseEntity<>(apiException, badRequest);
    }

    @ExceptionHandler(value = { CommonException.class })
    public ResponseEntity<Object> commonException(CommonException exception) {
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        ApiException apiException = new ApiException(
                badRequest,
                exception.getMessage(),
                ZonedDateTime.now(ZoneId.of("Z")));
        return new ResponseEntity<>(apiException, badRequest);
    }

}
