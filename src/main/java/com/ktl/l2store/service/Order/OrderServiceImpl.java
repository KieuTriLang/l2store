package com.ktl.l2store.service.Order;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.ktl.l2store.dto.ReqOrderDto;
import com.ktl.l2store.entity.ComboProduct;
import com.ktl.l2store.entity.Order;
import com.ktl.l2store.entity.Product;
import com.ktl.l2store.entity.User;
import com.ktl.l2store.exception.ItemNotfoundException;
import com.ktl.l2store.exception.ListException;
import com.ktl.l2store.repo.ComboProductRepo;
import com.ktl.l2store.repo.OrderRepo;
import com.ktl.l2store.repo.ProductRepo;
import com.ktl.l2store.repo.UserRepo;

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

    @Override
    public Order createOrder(String username, ReqOrderDto reqOrderDto) {

        User user = userRepo.findByUsername(username).orElseThrow(() -> new ItemNotfoundException("Not found user"));

        Order order = new Order();

        List<String> messages = new ArrayList<>();

        double total = 0;

        for (ReqOrderDto.Product item : reqOrderDto.getProducts()) {
            Product product = productRepo.findById(item.getId()).orElse(null);
            if (product.isLocked()) {
                messages.add(product.getName() + " has been discontinued!");
            } else {
                total = product.getPrice() * item.getQuantity();
            }
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
            total = cbProduct.getTotalPrice() * item.getQuantity();

        }
        if (messages.size() > 0) {
            throw new ListException(messages);
        }

        order.setOwner(user);

        order.setCashPaymentId(UUID.randomUUID().toString());

        order.setCreatedTime(ZonedDateTime.now(ZoneId.of("Z")));

        order.setTotal(total);

        return orderRepo.save(order);
    }

    @Override
    public void addPaypalPaymentId(Long id, String paypalPaymentId) {

        Order order = orderRepo.getById(id);
        order.setPaypalPaymentId(paypalPaymentId);
        orderRepo.save(order);
    }

    @Override
    public void updatePayedByPaypalPaymentId(String paypalPaymentId) {
        Order order = orderRepo.getByPaypalPaymentId(paypalPaymentId);
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
}
