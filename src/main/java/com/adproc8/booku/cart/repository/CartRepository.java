package com.adproc8.booku.cart.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.adproc8.booku.cart.model.Cart;
import com.adproc8.booku.cart.model.Cart.Id;

public interface CartRepository extends JpaRepository<Cart, Id> {
}
