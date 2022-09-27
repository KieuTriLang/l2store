package com.ktl.l2store.exception;

import java.util.List;

public class ListException extends RuntimeException {

    private List<String> messages;

    public ListException(List<String> messages) {
        this.messages = messages;
    }

    public List<String> getMessages() {
        return messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }
}
