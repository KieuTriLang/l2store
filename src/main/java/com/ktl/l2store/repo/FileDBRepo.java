package com.ktl.l2store.repo;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ktl.l2store.entity.FileDB;

public interface FileDBRepo extends JpaRepository<FileDB, Long> {
    Optional<FileDB> findByFileCode(Long fileCode);
}
