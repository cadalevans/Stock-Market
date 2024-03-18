package com.percianna.percianna.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;


@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
public class HistoricalData implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String spotName;

    private String spotAddress;

    private String spotCity;

    private String spotState;

    private int spotZipCode;

    private double spotLng;

    private double spotLat;


    @Column(name = "description")
    private String description;
    @ManyToMany
    private Set<User> Users;

}
