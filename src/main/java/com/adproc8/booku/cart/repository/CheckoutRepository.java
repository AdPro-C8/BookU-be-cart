package com.adproc8.booku.cart.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.adproc8.booku.cart.model.CartId;
import com.adproc8.booku.cart.model.Checkout;

public interface CheckoutRepository extends JpaRepository<Checkout, UUID> {
    Optional<Checkout> findByCartId(CartId cartId);
}
