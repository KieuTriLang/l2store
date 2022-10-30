package com.ktl.l2store.service.Token;

import com.ktl.l2store.entity.Token;

public interface TokenService {

    void saveToken(Token token);

    boolean confirmToken(String token);

    boolean resetToken(String token);
}
