package com.ktl.l2store.common;

import java.time.ZonedDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.val;

@Data
public class DataStatistic {
    public DataStatistic(String key, String value) {
        this.key = key;
        this.value = value;
    }

    private String key;
    private String value;
}
