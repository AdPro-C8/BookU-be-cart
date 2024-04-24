package com.adproc8.booku.cart.service;

import java.util.Optional;

import com.adproc8.booku.cart.model.Cart;
import com.adproc8.booku.cart.model.CartId;

public interface CartService {
    Cart save(Cart cart);
    Optional<Cart> findById(CartId cartId);
    void deleteById(CartId cartId);
}
