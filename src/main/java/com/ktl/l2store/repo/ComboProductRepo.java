package com.ktl.l2store.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ktl.l2store.entity.ComboProduct;

@Repository
public interface ComboProductRepo extends JpaRepository<ComboProduct, Long> {
    @Query(value = "SELECT DISTINCT c FROM #{#entityName} c WHERE c.name LIKE %:infix% AND c.totalPrice >= :priceStart AND c.totalPrice <= :priceEnd")
    Page<ComboProduct> findWithFilter(
            @Param("infix") String infix,
            @Param("priceStart") double priceStart,
            @Param("priceEnd") double priceEnd,
            Pageable pageable);

    Page<ComboProduct> findByOwnerUsername(String ownerName, Pageable pageable);
}
