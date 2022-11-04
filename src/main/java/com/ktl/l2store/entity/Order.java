package com.ktl.l2store.entity;

import java.time.ZonedDateTime;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.transaction.Transactional;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.security.access.method.P;

import com.ktl.l2store.common.OrderState;
import com.ktl.l2store.common.PaymentType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderCode;

    @OneToMany(fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    private Collection<OProduct> orderProducts;

    @OneToMany(fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    private Collection<OCombo> orderCombos;

    @Column(columnDefinition = "Decimal(3,2) default 0")
    private double shipping;

    @Column(columnDefinition = "Decimal(64,2) default 0")
    private double total;

    private boolean isPayed;

    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    @Column(name = "created_time")
    private ZonedDateTime createdTime;

    @Column(name = "payment_time")
    private ZonedDateTime paymentTime;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    private String payer;

    private String shippingAddress;

    private String recipientName;

    private String city;

    private String countryCode;

    private String postalCode;

    private String phone;

    private String paypalPaymentId;

    private String momoPaymentId;

    private String cashPaymentId;

    private String tokenId;

    @Enumerated(EnumType.STRING)
    private OrderState orderState;

}
