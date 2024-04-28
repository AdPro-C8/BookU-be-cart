package com.adproc8.booku.cart.model;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class Cart {

    @EmbeddedId
    private Id id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JsonIgnore
    private User user;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<Book> books;

    @Embeddable
    @Getter @Setter
    @NoArgsConstructor @AllArgsConstructor
    public static class Id implements Serializable {
        private UUID cartId;
        private UUID userId;

        public Id(UUID cartId) {
            this.cartId = cartId;
        }
    }
}
