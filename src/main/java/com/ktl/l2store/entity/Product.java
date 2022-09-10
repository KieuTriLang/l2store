package com.ktl.l2store.entity;

import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PostLoad;
import javax.persistence.PostUpdate;
import javax.persistence.Transient;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String overview;

    private String description;

    @OneToOne(cascade = CascadeType.ALL)
    private FileDB image;

    private double price;

    @ManyToMany(fetch = FetchType.EAGER)
    private Collection<Category> categories;

    @OneToMany()
    @Fetch(FetchMode.SUBSELECT)
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
        if (this.getEvaluates().size() > 50) {
            this.averageRate = this.getEvaluates().stream().mapToInt(e -> e.getStar()).average().orElse(5);
        }
    }

    @PostLoad
    public void postLoad() {
        this.amountOfEvaluate = this.getEvaluates().size();
    }

}
