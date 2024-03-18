package com.percianna.percianna.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class Recharge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private Long amount;
    private String currency;

    @Temporal(TemporalType.DATE)
    private Date transDate= new Date(System.currentTimeMillis());
    // @Enumerated(EnumType.STRING)
    /*@Enumerated(EnumType.STRING)
    private Status status;

     */
    private String paymentId;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


}
