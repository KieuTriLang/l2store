package com.ktl.l2store.entity;

import java.util.Collection;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Data
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(columnDefinition = "TEXT")
    private String overview;

    @Column(columnDefinition = "TEXT")
    private String detail;

    @OneToOne(cascade = CascadeType.ALL)
    private FileDB image;

    private int salesoff;

    private double price;

    @ManyToMany(fetch = FetchType.EAGER)
    private Collection<Category> categories;

    @OneToMany
    private Collection<Evaluate> evaluates;

    @Column(name = "total_purchases", columnDefinition = "integer default 0")
    private int totalPurchases;

    @Column(name = "average_rate", columnDefinition = "Decimal(3,1) default 5")
    private double averageRate;

    @Transient
    private int amountOfEvaluate;

    // @OneToMany()
    // @Fetch(FetchMode.SUBSELECT)
    // private Collection<FileDB> images;

    @Column(name = "locked", columnDefinition = "boolean default false")
    private boolean locked;

    @PostUpdate
    public void postUpdate() {
        if (this.getEvaluates() != null && this.getEvaluates().size() > 50) {
            this.averageRate = this.getEvaluates().stream().mapToInt(e -> e.getStar()).average().orElse(5);
        }
    }

    @PostLoad
    public void postLoad() {
        this.amountOfEvaluate = this.getEvaluates().size();
    }

}
