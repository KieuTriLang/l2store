package com.ktl.l2store.service.Token;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ktl.l2store.entity.Token;
import com.ktl.l2store.entity.User;
import com.ktl.l2store.exception.ItemNotfoundException;
import com.ktl.l2store.repo.TokenRepo;
import com.ktl.l2store.repo.UserRepo;

@Service
public class TokenServiceImpl implements TokenService {

    @Autowired
    private TokenRepo tokenRepo;

    @Autowired
    private UserRepo userRepo;

    @Override
    public void saveToken(Token token) {
        // TODO Auto-generated method stub
        tokenRepo.save(token);
    }

    @Override
    public boolean confirmToken(String token) {
        // TODO Auto-generated method stub
        Token record = tokenRepo.findByToken(token).orElseThrow(() -> new ItemNotfoundException("Token is not exist"));
        if (record.getExpiresAt().isBefore(ZonedDateTime.now(ZoneId.of("Z")))) {
            throw new ItemNotfoundException("Token is expired");
        }
        if (record.getUser().isEnable()) {
            return true;
        }
        if (record.getActivedAt() != null) {
            throw new ItemNotfoundException("Token is used");
        }

        record.setActivedAt(ZonedDateTime.now(ZoneId.of("Z")));
        User user = record.getUser();
        user.setEnable(true);
        userRepo.save(user);

        tokenRepo.save(record);

        return true;
    }

    @Override
    public boolean resetToken(String token) {
        // TODO Auto-generated method stub
        Token record = tokenRepo.findByToken(token).orElseThrow(() -> new ItemNotfoundException("Token is not exist"));
        if (record.getExpiresAt().isBefore(ZonedDateTime.now(ZoneId.of("Z")))) {
            throw new ItemNotfoundException("Token is expired");
        }
        if (record.getActivedAt() != null) {
            throw new ItemNotfoundException("Token is used");
        }

        record.setActivedAt(ZonedDateTime.now(ZoneId.of("Z")));
        tokenRepo.save(record);

        return true;
    }

}
