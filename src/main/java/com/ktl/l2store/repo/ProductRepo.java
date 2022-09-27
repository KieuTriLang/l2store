package com.ktl.l2store.repo;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ktl.l2store.entity.Product;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long> {

    Page<Product> findAll(Pageable pageable);

    Page<Product> findAllByLockedFalse(Pageable pageable);

    Page<Product> findByNameContaining(String infix, Pageable pageable);

    Page<Product> findByNameContainingAndLockedFalse(String infix, Pageable pageable);

    Page<Product> findByNameContainingAndCategoriesNameInAndAverageRateGreaterThanEqualAndPriceBetweenAndLocked(
            String infix,
            List<String> categoryNames,
            int star,
            double priceStart, double priceEnd, boolean locked, Pageable pageable);
}
