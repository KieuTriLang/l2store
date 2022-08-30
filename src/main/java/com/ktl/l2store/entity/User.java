package com.ktl.l2store.entity;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Data
public class User {
    public User(Object object, String string, String string2, String string3, String string4, boolean b,
            ZonedDateTime now, Object object2, ArrayList<Role> arrayList, ArrayList<Product> arrayList2,
            ArrayList<Bill> arrayList3, ZonedDateTime now2) {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    private String displayName;

    private String email;

    private String password;

    private boolean gender;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "avatar_id")
    @JsonIgnore
    private FileDB avatar;

    private String address;

    private ZonedDateTime dob;

    @ManyToMany(fetch = FetchType.EAGER)
    private Collection<Role> roles;

    @ManyToMany()
    @Fetch(FetchMode.SUBSELECT)
    private Collection<Product> favProducts;

    @OneToMany()
    @Fetch(FetchMode.SUBSELECT)
    private Collection<Bill> bills;

    @OneToMany()
    @Fetch(FetchMode.SUBSELECT)
    private Collection<ComboProduct> comboProducts;

    private ZonedDateTime updatedAt;
}
