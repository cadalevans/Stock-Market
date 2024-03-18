package com.percianna.percianna.Entity;

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
public class Order1 {
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
    @Temporal(TemporalType.DATE)
    private Date timestamp= new Date(System.currentTimeMillis());
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    private String enterprise;
    private double gainLoss;
    private double percentageGainLoss;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "sicav_id")
    private Sicav sicav;


}
