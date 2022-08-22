package com.ktl.l2store.entity;

import java.time.LocalDateTime;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String displayName;

    @Column(unique = true)
    private String email;

    private String password;

    private boolean gender;

    private LocalDateTime dob;

    @ManyToMany(fetch = FetchType.EAGER)
    private Collection<Role> roles;

    private LocalDateTime updatedAt;
}
