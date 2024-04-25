package com.adproc8.booku.cart.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Cart {

    @JsonIgnore
    @EmbeddedId
    private CartId id;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    private User user;

    @ManyToMany
    private List<Book> books;

}
