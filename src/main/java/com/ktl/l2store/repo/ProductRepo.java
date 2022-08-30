package com.ktl.l2store.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ktl.l2store.entity.Product;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long> {

}
