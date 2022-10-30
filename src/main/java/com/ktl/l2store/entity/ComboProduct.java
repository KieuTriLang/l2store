package com.ktl.l2store.entity;

import java.time.ZonedDateTime;
import java.util.Collection;

import javax.persistence.*;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Builder
@Table(name = "combo_products")
public class ComboProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @Fetch(FetchMode.SUBSELECT)
    private Collection<Product> products;

    private int salesoff;

    private double totalPrice;

    private int totalPurchases;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "owner_id", nullable = true)
    private User owner;

    @Column(name = "created_time")
    private ZonedDateTime createdTime;

    public void addTotalPurchases(int quantity) {
        this.totalPurchases += quantity;
        for (Product p : this.products) {
            p.addTotalPurchases(quantity);
        }
    }
}
