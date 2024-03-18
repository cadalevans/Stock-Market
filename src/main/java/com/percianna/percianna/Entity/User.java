package com.percianna.percianna.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;
    private String resetToken;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Portfolio> portfolios = new ArrayList<>();


    private LocalDateTime resetTokenExpiry;
    private double virtualBalance = 1000;
    //private double balance;
    private double totalBenefit;
    private double totalCurrentValue;
    private double totalInvestmentAmount;


    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Order1> orders1;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Recharge> recharge;
    @ManyToOne
    @JoinColumn(name = "sicav_id")
    private Sicav sicav;

}
