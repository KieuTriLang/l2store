package com.ktl.l2store.service.Order;

import org.springframework.data.domain.Pageable;

import java.util.List;

import org.springframework.data.domain.Page;

import com.ktl.l2store.common.DataStatistic;
import com.ktl.l2store.common.OrderState;
import com.ktl.l2store.common.PaymentType;
import com.ktl.l2store.dto.ReqOrderDto;
import com.ktl.l2store.entity.Order;
import com.paypal.api.payments.Payment;

public interface OrderService {

    List<DataStatistic> totalProductsByDate();

    List<DataStatistic> totalCombosByDate();

    List<DataStatistic> turnoverByDate();

    Page<Order> getAll(Pageable pageable);

    Order getById(Long id);

    Page<Order> getByOwnerUsername(String username, Pageable pageable);

    Order createOrder(String username, ReqOrderDto reqOrderDto);

    Order saveOrder(Order order);

    void deleteByTokenId(String tokenId);

    void addPaypalPaymentId(Long id, String paypalPaymentId);

    void updatePayedByPaypalPaymentId(String paypalPaymentId, PaymentType type, Payment payment);

    void updateOrderState(String orderCode, OrderState orderState);

    void seedOrderByUsername(String username, ReqOrderDto reqOrderDto);
}
