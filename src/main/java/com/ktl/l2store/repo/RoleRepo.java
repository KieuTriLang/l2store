package com.ktl.l2store.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ktl.l2store.entity.Role;

@Repository
public interface RoleRepo extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
