package com.adproc8.booku.cart.model;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Cart {

    @JsonIgnore
    @EmbeddedId
    private Id id;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    private User user;

    @ManyToMany
    private List<Book> books;

    @Embeddable
    @Getter @Setter
    @AllArgsConstructor
    public static class Id implements Serializable {
        private UUID cartId;
        private UUID userId;
    }
}
