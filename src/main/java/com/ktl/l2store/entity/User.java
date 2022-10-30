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
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(unique = true)
    private String username;

    private String email;

    private String password;

    private boolean gender;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "avatar_id")
    @Embedded
    private FileDB avatar;

    private String address;

    private ZonedDateTime dob;

    private boolean enable;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @Fetch(FetchMode.SUBSELECT)
    private Collection<Role> roles;

    @ManyToMany(cascade = CascadeType.ALL)
    private Collection<Product> favProducts;

    @OneToMany(mappedBy = "owner")
    private Collection<Order> orders;

    @ManyToMany(cascade = CascadeType.ALL)
    private Collection<ComboProduct> comboProducts;

    @OneToMany(mappedBy = "user")
    private Collection<Evaluate> evaluates;

    @OneToMany(mappedBy = "user")
    private Collection<Token> tokens;

    @Column(name = "update_at")
    private ZonedDateTime updatedAt;
}
