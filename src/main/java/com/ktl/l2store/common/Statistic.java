package com.ktl.l2store.common;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Statistic {
    private List<DataStatistic> productStatistic;
    private List<DataStatistic> comboStatistic;
    private List<DataStatistic> turnoverStatistic;

}
