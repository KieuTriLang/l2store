package com.ktl.l2store.service.Email;

public interface EmailService {

    void send(String to, String email);

    String buildEmail(String name, String link);

    String buildEmailReset(String name, String link);
}
