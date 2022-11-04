package com.ktl.l2store.dto;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Date;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.ktl.l2store.common.OrderState;
import com.ktl.l2store.common.PaymentType;
import com.ktl.l2store.entity.OProduct;
import com.ktl.l2store.entity.OCombo;

import lombok.Data;

@Data
public class OrderOverviewDto {

    private Long id;

    private String orderCode;

    private String buyer;

    private String payer;

    private int amountOfProduct;

    private int amountOfCombo;

    private double total;

    private boolean isPayed;

    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    private Date createdTime;

    private Date paymentTime;

    @Enumerated(EnumType.STRING)
    private OrderState orderState;

    public void setAmountOfP(Collection<OProduct> products) {
        this.amountOfProduct = products != null ? products.size() : 0;

    }

    public void setAmountOfC(Collection<OCombo> combos) {
        this.amountOfCombo = combos != null ? combos.size() : 0;

    }

    public void convertCreatedDate(ZonedDateTime date) {
        this.createdTime = Date.from(date.toInstant());
    }

    public void convertPaymentDate(ZonedDateTime date) {
        this.paymentTime = Date.from(date.toInstant());
    }

}
