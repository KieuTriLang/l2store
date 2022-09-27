package com.ktl.l2store.dto;

import java.util.List;

import com.paypal.api.payments.PayerInfo;
import com.paypal.api.payments.ShippingAddress;
import com.paypal.api.payments.Transaction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class PaypalPaymentDto {
    private PayerInfo payerInfo;
    private List<Transaction> transactions;
    private ShippingAddress shippingAddress;
}
