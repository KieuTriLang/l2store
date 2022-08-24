package com.ktl.l2store.entity;

import java.time.ZonedDateTime;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String billCode;

    @OneToMany(mappedBy = "bill", fetch = FetchType.EAGER)
    private Collection<BillProduct> cart;

    private int salesoff;

    private float total;

    private ZonedDateTime createdTime;
}
