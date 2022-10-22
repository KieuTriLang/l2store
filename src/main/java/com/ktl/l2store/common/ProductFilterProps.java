package com.ktl.l2store.common;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ProductFilterProps {

    private String name;

    private List<String> categoryNames;

    private int star;

    private double priceStart;

    private double priceEnd;
}
