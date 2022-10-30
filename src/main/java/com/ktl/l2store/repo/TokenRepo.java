package com.ktl.l2store.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ktl.l2store.entity.Token;

@Repository
public interface TokenRepo extends JpaRepository<Token, Long> {

    Optional<Token> findByToken(String token);
}
