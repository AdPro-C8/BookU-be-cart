package com.adproc8.booku.cart.model;

import java.util.UUID;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter @Setter
public class CartId {
    private UUID id;
    private UUID userId;
}
