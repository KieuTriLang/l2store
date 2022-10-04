package com.ktl.l2store.repo;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ktl.l2store.entity.User;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {

    Page<User> findAll(Pageable pageable);

    Page<User> findByUsernameContaining(String username, Pageable pageable);

    Optional<User> findByUsername(String username);

    boolean existsUserByUsername(String username);
}
