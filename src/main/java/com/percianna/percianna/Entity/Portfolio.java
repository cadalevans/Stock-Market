package com.percianna.percianna.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
public class Portfolio implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double actualPrice;


    // Many-to-One relationship with User
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private double totalReturn;
    private double percentageGainLoss;

   // @Temporal(TemporalType.TIMESTAMP)
    private Date purchaseDate;

    private double costBasis;


    private String stockSymbol;
    private String stockName;

    private double quantity;
    private String logo;
}
