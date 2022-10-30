package com.ktl.l2store.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ktl.l2store.entity.Order;

@Repository
public interface OrderRepo extends JpaRepository<Order, Long> {

    Page<Order> findAll(Pageable pageable);

    Page<Order> findByOwnerUsername(String username, Pageable pageable);

    Order findByPaypalPaymentId(String paypalPaymentId);

    Order findByTokenId(String tokenId);
}
