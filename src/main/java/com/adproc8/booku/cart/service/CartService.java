package com.adproc8.booku.cart.service;

import java.util.Optional;
import java.util.UUID;

import com.adproc8.booku.cart.model.Cart;

public interface CartService {
    Cart save(Cart cart);
    Optional<Cart> findById(UUID cartId, String username);
    Optional<Cart> findByUsername(String username);
    void deleteById(UUID cartId, String username);
}
