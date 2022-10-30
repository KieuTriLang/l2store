package com.ktl.l2store.dto;

import lombok.Data;

@Data
public class OrderItem {

    private String name;

    private double price;

    private int quantity;
}
