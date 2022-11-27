package com.ktl.l2store.api;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ktl.l2store.dto.ReqOrderDto;
import com.ktl.l2store.entity.ComboProduct;
import com.ktl.l2store.entity.Evaluate;
import com.ktl.l2store.entity.Product;
import com.ktl.l2store.entity.Role;
import com.ktl.l2store.entity.User;
import com.ktl.l2store.service.CbProduct.ComboProductService;
import com.ktl.l2store.service.Evaluate.EvaluateService;
import com.ktl.l2store.service.Order.OrderService;
import com.ktl.l2store.service.product.ProductService;
import com.ktl.l2store.service.user.UserService;

@RestController
@RequestMapping("/api/super-admin")
public class SuperAdminApi {

    @Autowired
    private UserService userService;
    @Autowired
    private EvaluateService evaluateService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ComboProductService comboProductService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/seed-data")
    public ResponseEntity<Object> seedData() {
        seedUser();
        seedEvaluate();
        seedOrder();
        return ResponseEntity.ok().build();
    }

    private void seedUser() {
        List<User> users = new ArrayList<User>();
        for (int i = 0; i < 100; i++) {
            User user = User.builder()
                    .id(null)
                    .firstName("account")
                    .lastName(i + "")
                    .username("account" + i)
                    .email("account" + i + "@gmail.com")
                    .password(passwordEncoder.encode("123456"))
                    .gender(i % 2 == 0)
                    .avatar(null)
                    .address("Earth number " + randomMinMax(1, 20))
                    .dob(ZonedDateTime.now(ZoneId.of("Z")).minusYears((long) randomMinMax(18, 65)))
                    .roles(new ArrayList<>())
                    .favProducts(new ArrayList<>())
                    .orders(new ArrayList<>())
                    .comboProducts(new ArrayList<>())
                    .enable(true)
                    .updatedAt(ZonedDateTime.now(ZoneId.of("Z")))
                    .build();
            users.add(user);
        }
        userService.saveMultiUser(users);
    }

    private void seedEvaluate() {
        String[] comments = new String[] { "I love it", "That's awesome", "I want it", "Ship to me now",
                "Pay it for me", "I've ever seen it before" };
        List<User> users = userService.getAllUser();
        int sizeUsers = users.size();
        List<Product> products = productService.getAll();
        List<Evaluate> evaluates = new ArrayList<>();
        for (Product product : products) {
            for (int i = 0; i < 6; i++) {
                Evaluate evaluate = Evaluate.builder()
                        .id(null)
                        .star(randomMinMax(3, 5))
                        .content(comments[randomMinMax(0, comments.length - 1)])
                        .postedTime(ZonedDateTime.now(ZoneId.of("Z")))
                        .user(users.get(randomMinMax(0, sizeUsers - 1)))
                        .product(product)
                        .build();
                evaluates.add(evaluate);
            }
        }
        evaluateService.saveMultiEvaluate(evaluates);
    }

    private void seedOrder() {
        List<User> users = userService.getAllUser();
        List<Product> products = productService.getAll();
        int sizeProducts = products.size();
        List<ComboProduct> comboProducts = comboProductService.getAll();
        int sizeCombos = comboProducts.size();
        for (User user : users) {

            for (int numOrder = 0; numOrder < randomMinMax(1, 100); numOrder++) {
                List<ReqOrderDto.Product> reqProducts = new ArrayList<>();
                for (int index = 0; index < randomMinMax(1, 5); index++) {
                    reqProducts.add(new ReqOrderDto.Product(products.get(randomMinMax(0, sizeProducts - 1)).getId(),
                            randomMinMax(5, 50)));
                }
                List<ReqOrderDto.Combo> reqCombos = new ArrayList<>();
                for (int i = 0; i < randomMinMax(0, 2); i++) {
                    reqCombos.add(new ReqOrderDto.Combo(comboProducts.get(randomMinMax(0, sizeCombos - 1)).getId(),
                            randomMinMax(5, 25)));
                }
                ReqOrderDto reqOrderDto = ReqOrderDto.builder().products(reqProducts).combos(reqCombos).build();
                orderService.seedOrderByUsername(user.getUsername(), reqOrderDto);
            }

        }
    }

    private int randomMinMax(int min, int max) {
        return (int) Math.floor(Math.random() * (max - min + 1) + min);
    }
}
