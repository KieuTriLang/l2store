package com.ktl.l2store.entity;

import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

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

    private String description;

    private String image;

    private float price;

    @ManyToMany(fetch = FetchType.EAGER)
    private Collection<Category> categories;

    @OneToMany()
    @Fetch(FetchMode.SUBSELECT)
    private Collection<Evaluate> evaluates;

    @OneToMany()
    @Fetch(FetchMode.SUBSELECT)
    private Collection<FileDB> images;

    private boolean isLocked;
}
