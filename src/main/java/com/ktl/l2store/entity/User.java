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
@Builder
@Entity
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    @Column(name = "display_name")
    private String displayName;

    private String email;

    private String password;

    private boolean gender;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "avatar_id")
    @Embedded
    private FileDB avatar;

    private String address;

    private ZonedDateTime dob;

    @ManyToMany(fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    private Collection<Role> roles;

    @ManyToMany()
    @Fetch(FetchMode.SUBSELECT)
    private Collection<Product> favProducts;

    @OneToMany()
    @Fetch(FetchMode.SUBSELECT)
    private Collection<Bill> bills;

    @OneToMany(mappedBy = "owner")
    @Fetch(FetchMode.SUBSELECT)
    private Collection<ComboProduct> comboProducts;

    @OneToMany(mappedBy = "user")
    private Collection<Evaluate> evaluates;

    @Column(name = "update_at")
    private ZonedDateTime updatedAt;
}
