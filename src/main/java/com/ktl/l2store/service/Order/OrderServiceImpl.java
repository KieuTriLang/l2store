package com.ktl.l2store.service.Order;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.ktl.l2store.common.DataStatistic;
import com.ktl.l2store.common.OrderState;
import com.ktl.l2store.common.PaymentType;
import com.ktl.l2store.dto.ReqOrderDto;
import com.ktl.l2store.entity.ComboProduct;
import com.ktl.l2store.entity.Order;
import com.ktl.l2store.entity.OCombo;
import com.ktl.l2store.entity.OProduct;
import com.ktl.l2store.entity.Product;
import com.ktl.l2store.entity.User;
import com.ktl.l2store.exception.ItemNotfoundException;
import com.ktl.l2store.exception.ListException;
import com.ktl.l2store.repo.ComboProductRepo;
import com.ktl.l2store.repo.OComboRepo;
import com.ktl.l2store.repo.OProductRepo;
import com.ktl.l2store.repo.OrderRepo;
import com.ktl.l2store.repo.ProductRepo;
import com.ktl.l2store.repo.UserRepo;
import com.paypal.api.payments.PayerInfo;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.ShippingAddress;
import com.paypal.api.payments.Transaction;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private ComboProductRepo cbProductRepo;

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    OProductRepo oProductRepo;
    @Autowired
    OComboRepo oComboRepo;

    @Override
    public Order createOrder(String username, ReqOrderDto reqOrderDto) {

        User user = userRepo.findByUsername(username).orElseThrow(() -> new ItemNotfoundException("Not found user"));

        Order order = new Order();

        order = orderRepo.save(order);

        List<OProduct> orderProducts = new ArrayList<>();
        List<OCombo> orderCombos = new ArrayList<>();

        List<String> messages = new ArrayList<>();

        double total = 0;

        for (ReqOrderDto.Product item : reqOrderDto.getProducts()) {
            Product product = productRepo.findById(item.getId()).orElse(null);
            if (product.isLocked()) {
                messages.add(product.getName() + " has been discontinued!");
            } else {
                total += (product.getPrice() - product.getPrice() * product.getSalesoff() / 100) * item.getQuantity();
            }
            OProduct op = oProductRepo
                    .save(OProduct.builder().product(product).quantity(item.getQuantity()).order(order).build());
            orderProducts
                    .add(op);
        }

        for (ReqOrderDto.Combo item : reqOrderDto.getCombos()) {

            ComboProduct cbProduct = cbProductRepo.findById(item.getId()).orElse(null);

            Collection<Product> productInCombo = cbProduct.getProducts();

            if (productInCombo.size() > 0) {
                for (Product product : productInCombo) {
                    if (product.isLocked()) {
                        messages.add(product.getName() + " in " + cbProduct.getName() + " has been discontinued!");
                    }
                }
            }
            total += (cbProduct.getTotalPrice() - cbProduct.getTotalPrice() * cbProduct.getSalesoff() / 100)
                    * item.getQuantity();

            OCombo ocb = oComboRepo
                    .save(OCombo.builder().comboProduct(cbProduct).quantity(item.getQuantity()).order(order).build());
            orderCombos.add(ocb);
        }
        if (messages.size() > 0) {
            throw new ListException(messages);
        }

        order.setOwner(user);

        order.setOrderCode(UUID.randomUUID().toString().replace("-", "").toUpperCase());
        // order.setCashPaymentId(UUID.randomUUID().toString());
        order.setOrderCombos(orderCombos);

        order.setOrderProducts(orderProducts);

        order.setCreatedTime(ZonedDateTime.now(ZoneId.of("Z")));

        order.setTotal(total);

        return orderRepo.save(order);
    }

    @Override
    public void addPaypalPaymentId(Long id, String paypalPaymentId) {
        Order order = orderRepo.findById(id).orElseThrow(() -> new ItemNotfoundException("not found order"));
        order.setPaypalPaymentId(paypalPaymentId);
        orderRepo.save(order);
    }

    @Override
    public void updatePayedByPaypalPaymentId(String paypalPaymentId, PaymentType type, Payment payment) {
        Order order = orderRepo.findByPaypalPaymentId(paypalPaymentId);

        List<OProduct> ops = order.getOrderProducts().stream().map(op -> {
            Product p = op.getProduct();
            p.addTotalPurchases(op.getQuantity());
            op.setProduct(productRepo.save(p));
            return op;
        }).toList();
        order.setOrderProducts(ops);
        List<OCombo> ocs = order.getOrderCombos().stream().map(oc -> {
            ComboProduct cp = oc.getComboProduct();
            cp.addTotalPurchases(oc.getQuantity());
            oc.setComboProduct(cbProductRepo.save(cp));
            return oc;
        }).toList();
        order.setOrderCombos(ocs);

        // if (order.getOrderProducts().size() > 0) {
        // for (OrderProduct op : order.getOrderProducts()) {
        // op.getProduct().addTotalPurchases(op.getQuantity());
        // }
        // }
        // if (order.getOrderCombos().size() > 0) {
        // for (OrderCombo oc : order.getOrderCombos()) {
        // oc.getComboProduct().addTotalPurchases(oc.getQuantity());
        // }
        // }

        PayerInfo payerInfo = payment.getPayer().getPayerInfo();

        List<Transaction> transactions = payment.getTransactions();

        ShippingAddress shippingAddress = transactions.get(0).getItemList().getShippingAddress();

        order.setPayer(String.format("%s %s", payerInfo.getFirstName(), payerInfo.getLastName()));

        order.setShippingAddress(shippingAddress.getLine1());

        order.setRecipientName(shippingAddress.getRecipientName());

        order.setCity(shippingAddress.getCity());

        order.setCountryCode(shippingAddress.getCountryCode());

        order.setPostalCode(shippingAddress.getPostalCode());

        order.setPhone(shippingAddress.getPhone());

        order.setOrderState(OrderState.WAITING_FOR_THE_GOODS);

        order.setPaymentTime(ZonedDateTime.now(ZoneId.of("Z")));

        order.setPaymentType(type);

        order.setPayed(true);

        orderRepo.save(order);
    }

    @Override
    public Page<Order> getAll(Pageable pageable) {

        return orderRepo.findAll(pageable);
    }

    @Override
    public Page<Order> getByOwnerUsername(String username, Pageable pageable) {

        return orderRepo.findByOwnerUsername(username, pageable);
    }

    @Override
    public Order getById(Long id) {

        return orderRepo.findById(id).orElseThrow(() -> new ItemNotfoundException("Order is not exist!"));
    }

    @Override
    public Order saveOrder(Order order) {
        // TODO Auto-generated method stub
        return orderRepo.save(order);
    }

    @Override
    public void deleteByTokenId(String tokenId) {
        // TODO Auto-generated method stub
        Order order = orderRepo.findByTokenId(tokenId);
        orderRepo.delete(order);
    }

    @Override
    public void updateOrderState(String orderCode, OrderState orderState) {
        // TODO Auto-generated method stub
        Order order = orderRepo.findByOrderCode(orderCode);
        order.setOrderState(orderState);
        orderRepo.save(order);

    }

    @Override
    public void seedOrderByUsername(String username, ReqOrderDto reqOrderDto) {
        // TODO Auto-generated method stub
        User user = userRepo.findByUsername(username).orElseThrow(() -> new ItemNotfoundException("Not found user"));

        Order order = new Order();

        order = orderRepo.save(order);
        List<OProduct> orderProducts = new ArrayList<>();
        List<OCombo> orderCombos = new ArrayList<>();

        List<String> messages = new ArrayList<>();

        double total = 0;

        for (ReqOrderDto.Product item : reqOrderDto.getProducts()) {
            Product product = productRepo.findById(item.getId()).orElse(null);
            if (product.isLocked()) {
                messages.add(product.getName() + " has been discontinued!");
            } else {
                total += (product.getPrice() - product.getPrice() * product.getSalesoff() / 100) * item.getQuantity();
            }
            product.addTotalPurchases(item.getQuantity());
            productRepo.save(product);
            OProduct op = oProductRepo
                    .save(OProduct.builder().product(product).quantity(item.getQuantity()).order(order).build());
            orderProducts
                    .add(op);
        }

        for (ReqOrderDto.Combo item : reqOrderDto.getCombos()) {

            ComboProduct cbProduct = cbProductRepo.findById(item.getId()).orElse(null);

            Collection<Product> productInCombo = cbProduct.getProducts();

            if (productInCombo.size() > 0) {
                for (Product product : productInCombo) {
                    if (product.isLocked()) {
                        messages.add(product.getName() + " in " + cbProduct.getName() + " has been discontinued!");
                    }
                    product.addTotalPurchases(item.getQuantity());
                }
                productRepo.saveAll(productInCombo);
            }
            total += (cbProduct.getTotalPrice() - cbProduct.getTotalPrice() * cbProduct.getSalesoff() / 100)
                    * item.getQuantity();

            OCombo ocb = oComboRepo
                    .save(OCombo.builder().comboProduct(cbProduct).quantity(item.getQuantity()).order(order).build());
            orderCombos.add(ocb);
        }
        if (messages.size() > 0) {
            throw new ListException(messages);
        }

        ZonedDateTime dateTime = ZonedDateTime.now(ZoneId.of("Z")).minusDays(randomMinMax(0, 365));
        order.setOwner(user);

        order.setOrderCode(UUID.randomUUID().toString().replace("-", "").toUpperCase());
        // order.setCashPaymentId(UUID.randomUUID().toString());
        order.setOrderCombos(orderCombos);

        order.setOrderProducts(orderProducts);

        order.setCreatedTime(dateTime);

        order.setTotal(total);

        order.setPayer(String.format("%s %s", "Kieu Tri", "Lang"));

        order.setShippingAddress("Ha Noi");

        order.setRecipientName(user.getUsername());

        order.setCity("Ha Noi");

        order.setCountryCode("VN");

        order.setPostalCode("21232");

        order.setPhone("12345678");

        order.setOrderState(OrderState.values()[randomMinMax(0, OrderState.values().length - 1)]);

        order.setPaymentTime(dateTime);

        order.setPaymentType(PaymentType.PAYPAL);

        order.setPayed(true);

        order.setPaypalPaymentId("PAYID-" + UUID.randomUUID().toString().replace("-", "").toUpperCase());
        order.setTokenId("EC-" + UUID.randomUUID().toString().replace("-", "").toUpperCase());

        orderRepo.save(order);
    }

    private int randomMinMax(int min, int max) {
        return (int) Math.floor(Math.random() * (max - min + 1) + min);
    }

    @Override
    public List<DataStatistic> totalProductsByDate() {
        // TODO Auto-generated method stub
        return oProductRepo.totalProductsByDate();
    }

    @Override
    public List<DataStatistic> totalCombosByDate() {
        // TODO Auto-generated method stub
        return oComboRepo.totalCombosByDate();
    }

    @Override
    public List<DataStatistic> turnoverByDate() {
        // TODO Auto-generated method stub
        return orderRepo.turnoverByDate();
    }
}
