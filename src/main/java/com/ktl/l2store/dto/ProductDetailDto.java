package com.ktl.l2store.dto;

import java.util.Collection;

import com.ktl.l2store.entity.Category;
import com.ktl.l2store.provider.URIBuilder;

import lombok.Data;

@Data
public class ProductDetailDto {

    private Long id;

    private String name;

    private String overview;

    private String detail;

    private String image;

    private int salesoff;

    private float price;

    private Collection<Category> categories;

    private double averageRate;

    private int amountOfEvaluate;

    private int sold;

    public void setImageUri(String imageCode) {
        this.image = URIBuilder.generate("/api/file/" + imageCode);
    }
}
