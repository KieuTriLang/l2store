package com.ktl.l2store.dto;

import java.time.ZonedDateTime;
import java.util.Collection;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.ktl.l2store.common.PaymentType;

import lombok.Data;

@Data
public class OrderDetailDto {

    private Long id;

    private Collection<OrderProductDto> orderProducts;

    private Collection<OrderComboDto> orderCombos;

    private double shipping;

    private double total;

    private boolean isPayed;

    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    private ZonedDateTime createdTime;

    private ZonedDateTime paymentTime;
}
