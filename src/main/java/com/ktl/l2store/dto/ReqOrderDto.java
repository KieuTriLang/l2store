package com.ktl.l2store.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReqOrderDto {

    @Data
    public static class Product {
        Long id;
        int quantity;
    }

    @Data
    public static class Combo {
        Long id;
        int quantity;
    }

    private List<Product> products;
    private List<Combo> combos;
}
