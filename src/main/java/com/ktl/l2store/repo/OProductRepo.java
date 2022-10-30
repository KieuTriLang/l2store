package com.ktl.l2store.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ktl.l2store.entity.OProduct;

@Repository
public interface OProductRepo extends JpaRepository<OProduct, Long> {

}
