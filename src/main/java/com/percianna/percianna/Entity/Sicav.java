package com.percianna.percianna.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Sicav {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String symbol;  // Symbol or code to uniquely identify the SICAV

    @Column(nullable = false)
    private String name;  // Name of the SICAV

    /*
    @Column(columnDefinition = "TEXT")
    private String description;  // Description or investment strategy of the SICAV

     */

    @Temporal(TemporalType.DATE)
    private Date creationDate= new Date(System.currentTimeMillis());


    private String about;

    @OneToMany(mappedBy = "sicav")
    private List<Stock> stocks;

    private double currentGrowthPercentage;
    private double totalAmount= 3000.0;
    private double enterPrice;
    private double firePrice;
    @OneToMany(mappedBy = "sicav")
    @JsonIgnore
    private List<User> users;
    @OneToMany(mappedBy = "sicav")
    @JsonIgnore
    private List<Order2> orders = new ArrayList<>();

    // Other relevant attributes:
    // - Fund Manager Information
    // - NAV (Net Asset Value) Calculation Logic
    // - Total Assets Under Management (AUM)
    // - Historical Performance Metrics
    // - Regulatory Compliance Details

    // Relationships:
    // - Many-to-One with Fund Manager
    // - One-to-Many with Portfolio (representing user holdings in the SICAV)

    // Getters and Setters...
   /* @OneToMany(mappedBy = "sicav", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SicavStock> sicavStocks = new ArrayList<>();


    public void addStock(Stock stock, int quantity) {
        SicavStock sicavStock = new SicavStock();
        sicavStock.setSicav(this);
        sicavStock.setStock(stock);
        sicavStock.setQuantity(quantity);

        sicavStocks.add(sicavStock);
        stock.getSicavStocks().add(sicavStock);
    }

    */
}