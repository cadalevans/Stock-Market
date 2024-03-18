package com.percianna.percianna.Entity;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
// Order related to sicav
public class Order2 {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /*
    @Enumerated(EnumType.STRING)
    private OrderType orderType;

     */
    @Enumerated(EnumType.STRING)
    private OrderSide orderSide;
    private int quantity;
    private Double buyingPrice;
    private Double price;
    private double olderPrice;
    private double gain;
    private double percentageChange;
    @Temporal(TemporalType.DATE)
    private Date timestamp= new Date(System.currentTimeMillis());
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    private String enterprise;

    @ManyToOne
    @JoinColumn(name = "sicav_id")
    private Sicav sicav;

}
