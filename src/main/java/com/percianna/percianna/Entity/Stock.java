package com.percianna.percianna.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Stock {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id

    private  Long Id;
    private String symbol;
    private String name;
    private double currentPrice;
    private double oldPrice;
    private double marketCap;
    private String logo;
    @ManyToOne
    @JoinColumn(name = "sicav_id")
    private Sicav sicav;
/*
    @OneToMany(mappedBy = "stock", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SicavStock> sicavStocks = new ArrayList<>();

 */

}
