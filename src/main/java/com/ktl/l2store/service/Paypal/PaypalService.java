package com.ktl.l2store.service.Paypal;

import com.ktl.l2store.entity.Order;
import com.ktl.l2store.entity.User;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;

public interface PaypalService {

    public Payment authorizePayment(User user, Order order) throws PayPalRESTException;

    public Payment getPaymentDetails(String paymentId) throws PayPalRESTException;

    public Payment executePayment(String paymentId, String payerId)
            throws PayPalRESTException;

    public String getApprovalLink(Payment approvedPayment);
}
