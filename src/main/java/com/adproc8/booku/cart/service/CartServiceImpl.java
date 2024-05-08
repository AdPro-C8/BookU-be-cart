package com.adproc8.booku.cart.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.adproc8.booku.cart.model.Cart;
import com.adproc8.booku.cart.repository.CartRepository;

@Service
class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;

    @Autowired
    CartServiceImpl(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public Cart save(Cart cart) throws IllegalArgumentException {
        return cartRepository.save(cart);
    }

    public Optional<Cart> findById(UUID cartId, String username) throws IllegalArgumentException {
        return cartRepository.findById(new Cart.Id(cartId, username));
    }

    public Optional<Cart> findByUsername(String username) throws IllegalArgumentException {
        return cartRepository.findByUsername(username);
    }

    public void deleteById(UUID cartId, String username) throws IllegalArgumentException {
        cartRepository.deleteById(new Cart.Id(cartId, username));
    }
}
