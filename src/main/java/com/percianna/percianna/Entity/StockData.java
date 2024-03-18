package com.percianna.percianna.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StockData {
    private String symbol;
    private double open;
    private double high;
    private double low;
    private double close;
    private long volume;

    // Constructors, getters, and setters
}
