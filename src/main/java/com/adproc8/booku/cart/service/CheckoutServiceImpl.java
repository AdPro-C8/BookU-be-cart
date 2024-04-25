package com.adproc8.booku.cart.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.adproc8.booku.cart.model.CartId;
import com.adproc8.booku.cart.model.Checkout;
import com.adproc8.booku.cart.repository.CheckoutRepository;

@Service
public class CheckoutServiceImpl implements CheckoutService {

    private final CheckoutRepository checkoutRepository;

    @Autowired
    public CheckoutServiceImpl(CheckoutRepository checkoutRepository) {
        this.checkoutRepository = checkoutRepository;
    }

    public Checkout save(Checkout checkout) throws IllegalArgumentException {
        return checkoutRepository.save(checkout);
    }

    public Optional<Checkout> findById(UUID id) throws IllegalArgumentException {
        return checkoutRepository.findById(id);
    }

    public Optional<Checkout> findByCartId(UUID cartId, UUID userId) throws IllegalArgumentException {
        CartId cartIdObject = new CartId();
        cartIdObject.setId(cartId);
        cartIdObject.setUserId(userId);
        return checkoutRepository.findByCartId(cartIdObject);
    }
}
