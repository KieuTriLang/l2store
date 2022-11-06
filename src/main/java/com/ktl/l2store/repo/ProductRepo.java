package com.ktl.l2store.repo;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ktl.l2store.entity.Product;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long> {

    Page<Product> findAll(Pageable pageable);

    List<Product> findByEvaluatesIdIn(List<Long> id);

    Page<Product> findAllByLockedFalse(Pageable pageable);

    Page<Product> findByNameContaining(String infix, Pageable pageable);

    Page<Product> findByNameContainingAndLockedFalse(String infix, Pageable pageable);

    @Query(value = "SELECT DISTINCT p FROM #{#entityName} p INNER JOIN p.categories c WHERE p.name LIKE %:infix% AND p.averageRate >= :star AND p.price >= :priceStart AND p.price <= :priceEnd AND c.name IN :categoryNames AND c.name IN :categoryNames")
    Page<Product> findWithFilter(
            @Param("infix") String infix,
            @Param("categoryNames") List<String> categoryNames,
            @Param("star") double star,
            @Param("priceStart") double priceStart,
            @Param("priceEnd") double priceEnd,
            Pageable pageable);
}
