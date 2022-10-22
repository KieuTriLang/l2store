package com.ktl.l2store.dto;

import java.util.Collection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReqCbProduct {

    private Long id;
    private String name;
    private String description;
    private Collection<Long> productIds;
    private int salesoff;
    private float total;
}
