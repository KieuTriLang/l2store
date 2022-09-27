package com.ktl.l2store.dto;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.ktl.l2store.common.PaymentType;

import lombok.Data;

@Data
public class OrderOverviewDto {

    private Long id;

    private int amountOfProduct;

    private int amountOfCombo;

    private double total;

    private boolean isPayed;

    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;
}
