package com.ktl.l2store.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ktl.l2store.common.DataStatistic;
import com.ktl.l2store.entity.Order;

@Repository
public interface OrderRepo extends JpaRepository<Order, Long> {

    Page<Order> findAll(Pageable pageable);

    Page<Order> findByOwnerUsername(String username, Pageable pageable);

    Order findByPaypalPaymentId(String paypalPaymentId);

    Order findByTokenId(String tokenId);

    Order findByOrderCode(String orderCode);

    @Query(value = "SELECT " +
            "    new com.ktl.l2store.common.DataStatistic(CAST(o.paymentTime AS string), CAST(SUM(o.total) AS string)) "
            + "FROM "
            + "    #{#entityName} o "
            + "GROUP BY "
            + "    DATE_FORMAT(o.paymentTime, '%d-%m-%y') "
            + "ORDER BY "
            + "    DATE(o.paymentTime) DESC")
    List<DataStatistic> turnoverByDate();
}
