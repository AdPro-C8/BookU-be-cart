package com.adproc8.booku.cart.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.adproc8.booku.cart.model.Cart;
import com.adproc8.booku.cart.model.CartId;
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

    public Optional<Cart> findById(UUID cartId, UUID userId) throws IllegalArgumentException {
        CartId cartIdObject = new CartId();
        cartIdObject.setId(cartId);
        cartIdObject.setUserId(userId);
        return cartRepository.findById(cartIdObject);
    }

    public void deleteById(UUID cartId, UUID userId) throws IllegalArgumentException {
        CartId cartIdObject = new CartId();
        cartIdObject.setId(cartId);
        cartIdObject.setUserId(userId);
        cartRepository.deleteById(cartIdObject);
    }
}
