package com.ktl.l2store.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ktl.l2store.entity.ComboProduct;

@Repository
public interface ComboProductRepo extends JpaRepository<ComboProduct, Long> {

    Page<ComboProduct> findByNameContainingAndTotalPriceBetween(String name, double priceStart, double priceEnd,
            Pageable pageable);

    Page<ComboProduct> findByOwnerUsername(String ownerName, Pageable pageable);
}
