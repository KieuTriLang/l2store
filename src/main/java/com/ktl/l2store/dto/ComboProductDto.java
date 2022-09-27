package com.ktl.l2store.dto;

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
public class ComboProductDto {

    private Long id;

    private String name;

    private String description;

    private Collection<String> productImages;

    private double totalPrice;

    public void setProductImages(Collection<Product> products) {
        products.stream()
                .map(item -> this.productImages.add(URIBuilder.generate("/api/file" + item.getImage().getFileCode())));
    }
}
