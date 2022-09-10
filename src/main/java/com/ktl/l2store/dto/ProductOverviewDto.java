package com.ktl.l2store.dto;

import com.ktl.l2store.provider.URIBuilder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductOverviewDto {
    private Long id;

    private String name;

    private String overview;

    private String image;

    private float averageRate;

    private int amountOfEvaluate;

    private float price;

    public void setImageUri(String imageCode) {
        this.image = URIBuilder.generate("/api/file/" + imageCode);
    }
}
