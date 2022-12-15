package com.ktl.l2store.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ReqOrderDto {

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class Product {
        Long id;
        int quantity;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class Combo {
        Long id;
        int quantity;
    }

    private List<Product> products;
    private List<Combo> combos;
}
