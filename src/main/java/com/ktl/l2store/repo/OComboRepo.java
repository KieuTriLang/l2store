package com.ktl.l2store.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ktl.l2store.common.DataStatistic;
import com.ktl.l2store.entity.OCombo;

@Repository
public interface OComboRepo extends JpaRepository<OCombo, Long> {

    @Query(value = "SELECT " +
            "    new com.ktl.l2store.common.DataStatistic(CAST(o.paymentTime AS string), CAST(COUNT(oc.quantity) AS string)) "
            +
            "FROM " +
            "    #{#entityName} oc " +
            "INNER JOIN " +
            "    oc.order o " +
            "GROUP BY "
            + "    DATE_FORMAT(o.paymentTime, '%d-%m-%y') "
            + "ORDER BY "
            + "    DATE(o.paymentTime) DESC")
    List<DataStatistic> totalCombosByDate();
}
