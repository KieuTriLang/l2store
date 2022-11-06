package com.ktl.l2store.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ktl.l2store.entity.Evaluate;
import com.ktl.l2store.entity.Product;
import com.ktl.l2store.entity.User;

@Repository
public interface EvaluateRepo extends JpaRepository<Evaluate, Long> {

    Page<Evaluate> findAll(Pageable pageable);

    Page<Evaluate> findByProduct(Product product, Pageable pageable);

    List<Evaluate> findByProduct(Product product);

    Page<Evaluate> findByUser(User user, Pageable pageable);

    Page<Evaluate> findByContentContaining(String search, Pageable pageable);

    @Query(value = "SELECT AVG(e.star) FROM #{#entityName} e WHERE e.product_id = :idP", nativeQuery = true)
    double getAvgRateOfProduct(@Param("idP") Long idP);

    List<Evaluate> findByIdIn(List<Long> ids);
    // double averageRateByProduct(Product product);
}
