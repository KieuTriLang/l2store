package com.ktl.l2store.dto;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Date;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.ktl.l2store.common.PaymentType;
import com.ktl.l2store.entity.User;

import lombok.Data;

@Data
public class OrderDetailDto {

    private Long id;

    private Collection<OrderItem> orderProducts;

    private Collection<OrderItem> orderCombos;

    private double shipping;

    private double total;

    private boolean isPayed;

    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    private Date createdTime;

    private Date paymentTime;

    private String payer;

    public void convertCreatedDate(ZonedDateTime date) {
        this.createdTime = Date.from(date.toInstant());
    }

    public void convertPaymentDate(ZonedDateTime date) {
        this.paymentTime = Date.from(date.toInstant());
    }

    public void setPayerName(User user) {
        this.payer = user.getUsername();
    }
}
