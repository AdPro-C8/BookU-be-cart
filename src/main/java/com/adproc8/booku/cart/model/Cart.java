package com.adproc8.booku.cart.model;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@IdClass(CartId.class)
@Getter @Setter
public class Cart {

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @JsonIgnore
    @Id
    @OneToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToMany
    private List<Book> books;

}
