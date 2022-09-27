package com.ktl.l2store.api;

import java.util.Collection;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ktl.l2store.dto.OrderComboDto;
import com.ktl.l2store.dto.OrderDetailDto;
import com.ktl.l2store.dto.OrderOverviewDto;
import com.ktl.l2store.dto.OrderProductDto;
import com.ktl.l2store.dto.PaypalPaymentDto;
import com.ktl.l2store.dto.ReqOrderDto;
import com.ktl.l2store.entity.Order;
import com.ktl.l2store.entity.User;
import com.ktl.l2store.provider.AuthorizationHeader;
import com.ktl.l2store.service.Order.OrderService;
import com.ktl.l2store.service.Paypal.PaypalService;
import com.ktl.l2store.service.user.UserService;
import com.ktl.l2store.utils.PagingParam;
import com.paypal.api.payments.PayerInfo;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.ShippingAddress;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.PayPalRESTException;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/orders")
public class OrderApi {

    @Autowired
    private PaypalService paymentService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ModelMapper modelMapper;

    // Get all
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public ResponseEntity<Object> getAll(@PagingParam Pageable pageable) {
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/my-orders", method = RequestMethod.GET)
    public ResponseEntity<Object> getOwnOrde(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @PagingParam Pageable pageable) {

        String username = AuthorizationHeader.getSub(authorizationHeader);

        Page<Order> orders = orderService.getByOwnerUsername(username, pageable);

        List<OrderOverviewDto> orderOverviewDtos = orders.stream()
                .map(order -> modelMapper.map(order, OrderOverviewDto.class)).toList();

        Page<OrderOverviewDto> resPageDto = new PageImpl<>(orderOverviewDtos, pageable, orders.getTotalElements());

        return ResponseEntity.ok().body(resPageDto);
    }

    // Get by billcode
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Object> getByOrderCode(@PathVariable("id") Long id) {

        Order order = orderService.getById(id);

        OrderDetailDto orderDetailDto = modelMapper.map(order, OrderDetailDto.class);

        Collection<OrderComboDto> orderComboDtos = order.getOrderCombos().stream()
                .map(oc -> modelMapper.map(oc, OrderComboDto.class)).toList();

        Collection<OrderProductDto> orderProductDtos = order.getOrderProducts().stream()
                .map(op -> modelMapper.map(op, OrderProductDto.class)).toList();

        orderDetailDto.setOrderCombos(orderComboDtos);

        orderDetailDto.setOrderProducts(orderProductDtos);

        return ResponseEntity.ok().body(orderDetailDto);
    }

    // paypal authorize
    @RequestMapping(value = "/paypal/authorize-payment", method = RequestMethod.POST)
    public ResponseEntity<Object> authorizePayment(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @RequestBody ReqOrderDto reqOrderDto) {

        String username = AuthorizationHeader.getSub(authorizationHeader);

        Order order = orderService.createOrder(username, reqOrderDto);

        User user = userService.getUser(username);

        try {

            Payment approvedPayment = paymentService.authorizePayment(user, order);

            orderService.addPaypalPaymentId(order.getId(), approvedPayment.getId());

            String approvalLink = paymentService.getApprovalLink(approvedPayment);

            return ResponseEntity.status(HttpStatus.FOUND).body(approvalLink);

        } catch (PayPalRESTException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // paypal review
    @RequestMapping(value = "/paypal/review-payment", method = RequestMethod.GET)
    public ResponseEntity<Object> reviewPayment(@RequestParam("paymentId") String paymentId,
            @RequestParam("PayerId") String payerId) {
        try {
            Payment payment = paymentService.getPaymentDetails(paymentId);

            PayerInfo payerInfo = payment.getPayer().getPayerInfo();

            List<Transaction> transactions = payment.getTransactions();

            ShippingAddress shippingAddress = transactions.get(0).getItemList().getShippingAddress();

            PaypalPaymentDto paypalPaymentDto = PaypalPaymentDto.builder()
                    .payerInfo(payerInfo)
                    .transactions(transactions)
                    .shippingAddress(shippingAddress).build();

            return ResponseEntity.ok(paypalPaymentDto);
        } catch (PayPalRESTException e) {
            // TODO: handle exception
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // paypal excute
    @RequestMapping(value = "/paypal/excute-payment", method = RequestMethod.GET)
    public ResponseEntity<Object> excutePayment(@RequestParam("paymentId") String paymentId,
            @RequestParam("PayerId") String payerId) {

        try {
            Payment payment = paymentService.executePayment(paymentId, payerId);

            PayerInfo payerInfo = payment.getPayer().getPayerInfo();

            List<Transaction> transactions = payment.getTransactions();

            PaypalPaymentDto paypalPaymentDto = PaypalPaymentDto.builder()
                    .payerInfo(payerInfo)
                    .transactions(transactions)
                    .build();

            return ResponseEntity.ok(paypalPaymentDto);

        } catch (PayPalRESTException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

    }

}
