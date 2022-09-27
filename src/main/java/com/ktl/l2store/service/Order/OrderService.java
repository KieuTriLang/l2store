package com.ktl.l2store.service.Order;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import com.ktl.l2store.dto.ReqOrderDto;
import com.ktl.l2store.entity.Order;

public interface OrderService {

    Page<Order> getAll(Pageable pageable);

    Order getById(Long id);

    Page<Order> getByOwnerUsername(String username, Pageable pageable);

    Order createOrder(String username, ReqOrderDto reqOrderDto);

    void addPaypalPaymentId(Long id, String paypalPaymentId);

    void updatePayedByPaypalPaymentId(String paypalPaymentId);
}
