package com.ktl.l2store.exception;

public class ItemExistException extends RuntimeException {
    public ItemExistException(String message) {
        super(message);
    }
}
