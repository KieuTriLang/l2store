package com.ktl.l2store.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ktl.l2store.entity.User;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {

    List<User> findByRolesName(String nameRole);

    Page<User> findAll(Pageable pageable);

    Page<User> findByUsernameContaining(String username, Pageable pageable);

    Page<User> findByRolesName(String nameRole, Pageable pageable);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    boolean existsUserByUsername(String username);

    boolean existsUserByEmail(String email);
}
