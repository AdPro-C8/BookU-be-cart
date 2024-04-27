package com.adproc8.booku.cart.service;

import java.util.Optional;
import java.util.UUID;

import com.adproc8.booku.cart.model.Checkout;

public interface CheckoutService {
    Checkout save(Checkout checkout);
    Optional<Checkout> findById(UUID id);
}
