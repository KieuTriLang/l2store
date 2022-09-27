package com.ktl.l2store.dto;

import lombok.Data;

@Data
public class OrderProductDto {

    private String nameProduct;

    private double price;

    private int quantity;
}
