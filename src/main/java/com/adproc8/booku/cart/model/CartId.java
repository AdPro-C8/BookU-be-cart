package com.adproc8.booku.cart.model;

import java.io.Serializable;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CartId implements Serializable {
    private UUID id;
    private User user;
}
