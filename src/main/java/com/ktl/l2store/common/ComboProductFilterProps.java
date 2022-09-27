package com.ktl.l2store.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ComboProductFilterProps {

    private String name;

    private double priceStart;

    private double priceEnd;

}
