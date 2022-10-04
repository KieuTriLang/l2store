package com.ktl.l2store.service.Paypal;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ktl.l2store.entity.Order;
import com.ktl.l2store.entity.OrderCombo;
import com.ktl.l2store.entity.OrderProduct;
import com.ktl.l2store.entity.User;
import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Details;
import com.paypal.api.payments.Item;
import com.paypal.api.payments.ItemList;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.PayerInfo;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.api.payments.RedirectUrls;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;

@Service
public class PaypalServiceImpl implements PaypalService {

    @Value("${paypal.mode}")
    private String MODE;
    @Value("${paypal.client.id}")
    private String CLIENT_ID;
    @Value("${paypal.client.secret}")
    private String CLIENT_SECRET;

    @Override
    public Payment authorizePayment(User user, Order order) throws PayPalRESTException {

        Payer payer = getPayerInformation(user);
        RedirectUrls redirectUrls = getRedirectURLs();
        List<Transaction> listTransaction = getTransactionInformation(order);

        Payment requestPayment = new Payment();
        requestPayment.setTransactions(listTransaction);
        requestPayment.setRedirectUrls(redirectUrls);
        requestPayment.setPayer(payer);
        requestPayment.setIntent("authorize");

        APIContext apiContext = new APIContext(CLIENT_ID, CLIENT_SECRET, MODE);

        Payment approvedPayment = requestPayment.create(apiContext);

        return approvedPayment;
    }

    @Override
    public Payment getPaymentDetails(String paymentId) throws PayPalRESTException {

        APIContext apiContext = new APIContext(CLIENT_ID, CLIENT_SECRET, MODE);
        return Payment.get(apiContext, paymentId);
    }

    @Override
    public Payment executePayment(String paymentId, String payerId)
            throws PayPalRESTException {
        PaymentExecution paymentExecution = new PaymentExecution();

        paymentExecution.setPayerId(payerId);

        Payment payment = new Payment().setId(paymentId);

        APIContext apiContext = new APIContext(CLIENT_ID, CLIENT_SECRET, MODE);

        return payment.execute(apiContext, paymentExecution);
    }

    private Payer getPayerInformation(User user) {
        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");

        PayerInfo payerInfo = new PayerInfo();
        payerInfo.setFirstName(user.getFirstName())
                .setLastName(user.getLastName())
                .setEmail(user.getEmail());

        payer.setPayerInfo(payerInfo);

        return payer;
    }

    private RedirectUrls getRedirectURLs() {

        RedirectUrls redirectUrls = new RedirectUrls();

        redirectUrls.setCancelUrl("");

        redirectUrls.setReturnUrl("http://localhost:8080/api/orders/paypal/review-payment");

        return redirectUrls;
    }

    private List<Transaction> getTransactionInformation(Order order) {

        Details details = new Details();

        double totalProduct = order.getOrderProducts().stream()
                .mapToDouble(p -> p.getProduct().getPrice() * p.getQuantity()).sum();

        double totalCombo = order.getOrderCombos().stream()
                .mapToDouble(cb -> cb.getComboProduct().getTotalPrice() * cb.getQuantity()).sum();

        String subTotal = Double.toString(totalProduct + totalCombo);

        details.setSubtotal(subTotal);

        details.setShipping(Double.toString(order.getShipping()));

        Amount amount = new Amount();

        amount.setCurrency("USD");

        amount.setTotal(Double.toString(order.getTotal()));

        amount.setDetails(details);

        Transaction transaction = new Transaction();

        transaction.setAmount(amount);

        // transaction.setDescription(orderDetail.getProductName());

        ItemList itemList = new ItemList();
        List<Item> items = new ArrayList<>();

        for (OrderProduct op : order.getOrderProducts()) {

            Item item = new Item();

            item.setCurrency("USD");

            item.setName(op.getProduct().getName());

            item.setPrice(Double.toString(op.getProduct().getPrice()));

            item.setQuantity(Integer.toString(op.getQuantity()));

            items.add(item);
        }

        for (OrderCombo oCb : order.getOrderCombos()) {

            Item item = new Item();

            item.setCurrency("USD");

            item.setName(oCb.getComboProduct().getName());

            item.setPrice(Double.toString(oCb.getComboProduct().getTotalPrice()));

            item.setQuantity(Integer.toString(oCb.getQuantity()));

            items.add(item);
        }

        itemList.setItems(items);
        transaction.setItemList(itemList);

        List<Transaction> listTransaction = new ArrayList<>();
        listTransaction.add(transaction);

        return listTransaction;
    }

    public String getApprovalLink(Payment approvedPayment) {
        List<Links> links = approvedPayment.getLinks();
        String approvalLink = null;

        for (Links link : links) {
            if (link.getRel().equalsIgnoreCase("approval_url")) {
                approvalLink = link.getHref();
                break;
            }
        }

        return approvalLink;
    }

}
