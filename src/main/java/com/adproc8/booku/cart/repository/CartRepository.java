package com.adproc8.booku.cart.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.adproc8.booku.cart.model.Cart;

public interface CartRepository extends JpaRepository<Cart, UUID> {
}
