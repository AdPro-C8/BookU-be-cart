package com.adproc8.booku.cart.service;

import java.util.Optional;
import java.util.UUID;

import com.adproc8.booku.cart.model.Cart;

public interface CartService {
    Cart save(Cart cart);
    Optional<Cart> findById(UUID id);
    void deleteById(UUID id);
}