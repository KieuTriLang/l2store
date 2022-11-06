package com.ktl.l2store.dto;

import java.util.Collection;

import com.ktl.l2store.entity.Category;
import com.ktl.l2store.entity.Evaluate;
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

    private int salesoff;

    private float price;

    private Collection<Category> categories;

    private int sold;

    public void setImageUri(String imageCode) {
        this.image = URIBuilder.generate("/api/file/" + imageCode);
    }

    public void setaoe(Collection<Evaluate> evaluates) {
        this.amountOfEvaluate = evaluates != null ? evaluates.size() : 0;
    }
}
