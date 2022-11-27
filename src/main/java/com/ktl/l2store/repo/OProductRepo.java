package com.ktl.l2store.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ktl.l2store.common.DataStatistic;
import com.ktl.l2store.entity.OProduct;

@Repository
public interface OProductRepo extends JpaRepository<OProduct, Long> {

    @Query(value = "SELECT " +
            "    new com.ktl.l2store.common.DataStatistic(CAST(o.paymentTime AS string), CAST(COUNT(op.quantity) AS string)) "
            +
            "FROM " +
            "    #{#entityName} op " +
            "INNER JOIN " +
            "    op.order o " +
            "GROUP BY "
            + "    DATE_FORMAT(o.paymentTime, '%d-%m-%y') "
            + "ORDER BY "
            + "    DATE(o.paymentTime) DESC")
    List<DataStatistic> totalProductsByDate();
}
