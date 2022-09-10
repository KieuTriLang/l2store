package com.ktl.l2store.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ktl.l2store.entity.Evaluate;
import com.ktl.l2store.entity.Product;
import com.ktl.l2store.entity.User;

@Repository
public interface EvaluateRepo extends JpaRepository<Evaluate, Long> {

    Page<Evaluate> findByProduct(Product product, Pageable pageable);

    Page<Evaluate> findByUser(User user, Pageable pageable);
}
