package com.ktl.l2store.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ktl.l2store.entity.Category;

@Repository
public interface CategoryRepo extends JpaRepository<Category, Long> {

    Page<Category> findAllByIsLockedFalse(Pageable pageable);
}
