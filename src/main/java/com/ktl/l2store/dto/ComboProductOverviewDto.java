package com.ktl.l2store.dto;

import java.util.ArrayList;
import java.util.Collection;

import com.ktl.l2store.entity.Product;
import com.ktl.l2store.provider.URIBuilder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ComboProductOverviewDto {

    private Long id;

    private String name;

    private String description;

    private Collection<String> productImages;

    private int salesoff;

    private double totalPrice;

    private int totalPurchases;

    public void setProductImagesUrl(Collection<Product> products) {
        this.productImages = products.stream()
                .map(item -> URIBuilder.generate("/api/file/" + item.getImage().getFileCode())).toList();
    }
}
