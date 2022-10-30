package com.ktl.l2store.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReqEvaluate {
    private Long id;
    private int star;
    private String content;
}
