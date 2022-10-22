package com.ktl.l2store.dto;

import java.util.Collection;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;

import com.ktl.l2store.entity.Product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ComboProductDetailDto {

    private Long id;

    private String name;

    private String description;

    private Collection<ProductOverviewDto> productOverviews;

    private int salesoff;

    private double totalPrice;

    // public void setProductOverviewDto(ModelMapper mapper, Collection<Product>
    // products){
    // this.productOverviews = products.stream().map(p -> mapper.map(p,
    // ProductOverviewDto.class)).toList();
    // }
}
