package com.ktl.l2store.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ktl.l2store.common.DataStatistic;
import com.ktl.l2store.common.OrderState;
import com.ktl.l2store.common.PaymentType;
import com.ktl.l2store.common.Statistic;
import com.ktl.l2store.dto.OrderDetailDto;
import com.ktl.l2store.dto.OrderItem;
import com.ktl.l2store.dto.OrderOverviewDto;
import com.ktl.l2store.dto.PaypalPaymentDto;
import com.ktl.l2store.dto.PaypalRes;
import com.ktl.l2store.dto.ReqExcutePayment;
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
import org.springframework.web.bind.annotation.RequestPart;

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
    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<Object> getAll(@PagingParam Pageable pageable) {
        Page<Order> orders = orderService.getAll(pageable);

        List<OrderOverviewDto> orderOverviewDtos = orders.stream()
                .map(order -> modelMapper.map(order, OrderOverviewDto.class)).toList();

        Page<OrderOverviewDto> resPageDto = new PageImpl<>(orderOverviewDtos, pageable, orders.getTotalElements());

        return ResponseEntity.ok(resPageDto);
    }

    @RequestMapping(value = "/order-state", method = RequestMethod.GET)
    public ResponseEntity<Object> getOrderState() {
        return ResponseEntity.ok(Arrays.asList(OrderState.values()));
    }

    @RequestMapping(value = "/order-state", method = RequestMethod.PUT)
    public ResponseEntity<Object> updateOrderState(@RequestParam String orderCode,
            @RequestParam OrderState orderState) {
        orderService.updateOrderState(orderCode, orderState);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @RequestMapping(value = "/my-orders", method = RequestMethod.GET)
    public ResponseEntity<Object> getOwnOrder(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @PagingParam Pageable pageable) {

        String username = AuthorizationHeader.getSub(authorizationHeader);

        Page<Order> orders = orderService.getByOwnerUsername(username, pageable);

        List<OrderOverviewDto> orderOverviewDtos = orders.stream()
                .map(order -> modelMapper.map(order, OrderOverviewDto.class)).toList();

        Page<OrderOverviewDto> resPageDto = new PageImpl<>(orderOverviewDtos, pageable, orders.getTotalElements());

        return ResponseEntity.status(HttpStatus.OK).body(resPageDto);
    }

    // Get by billcode
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Object> getByOrderCode(@PathVariable("id") Long id) {

        Order order = orderService.getById(id);

        OrderDetailDto orderDetailDto = modelMapper.map(order, OrderDetailDto.class);

        Collection<OrderItem> orderComboDtos = order.getOrderCombos().stream()
                .map(oc -> modelMapper.map(oc, OrderItem.class)).toList();

        Collection<OrderItem> orderProductDtos = order.getOrderProducts().stream()
                .map(op -> modelMapper.map(op, OrderItem.class)).toList();

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

            // orderService.addPaypalPaymentId(order.getId(), approvedPayment.getId());

            String approvalLink = paymentService.getApprovalLink(approvedPayment);

            Pattern MY_PATTERN = Pattern.compile("token=EC\\-[a-zA-Z0-9]+");

            Matcher m = MY_PATTERN.matcher(approvalLink);
            String tokenId = null;
            while (m.find()) {
                tokenId = m.group(0);
                // s now contains "BAR"
            }

            if (tokenId != null) {
                tokenId = tokenId.substring(6);
                order.setTokenId(tokenId);
                order.setPaypalPaymentId(approvedPayment.getId());
                orderService.saveOrder(order);
            }
            return ResponseEntity.status(HttpStatus.OK).body(PaypalRes.builder().link(approvalLink).build());

        } catch (PayPalRESTException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @RequestMapping(value = "/paypal/cancel-payment/{token}", method = RequestMethod.GET)
    public ResponseEntity<Object> cancelPayment(@PathVariable String token) {
        orderService.deleteByTokenId(token);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // paypal review
    @RequestMapping(value = "/paypal/review-payment", method = RequestMethod.GET)
    public ResponseEntity<Object> reviewPayment(@RequestParam("paymentId") String paymentId,
            @RequestParam("PayerID") String payerId) {
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
    @RequestMapping(value = "/paypal/excute-payment", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> excutePayment(@RequestBody ReqExcutePayment req) {

        try {
            Payment payment = paymentService.executePayment(req.getPaymentId(), req.getPayerId());
            orderService.updatePayedByPaypalPaymentId(req.getPaymentId(), PaymentType.PAYPAL, payment);

            return ResponseEntity.status(HttpStatus.OK).build();

        } catch (PayPalRESTException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

    }

    @RequestMapping(value = "/data-order-statistic", method = RequestMethod.GET)
    public ResponseEntity<Object> dataOrderStatistic() {
        Statistic statistic = new Statistic(orderService.totalProductsByDate(), orderService.totalCombosByDate(),
                orderService.turnoverByDate());
        return ResponseEntity.status(HttpStatus.OK).body(statistic);
    }

}
