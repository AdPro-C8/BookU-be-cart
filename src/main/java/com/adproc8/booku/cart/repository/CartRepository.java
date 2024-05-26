package com.adproc8.booku.cart.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.adproc8.booku.cart.model.Cart;

public interface CartRepository extends JpaRepository<Cart, UUID> {
    @Query("select c from Cart c where c.userId = ?1 and c.active = true")
    Optional<Cart> findByUserId(UUID userId);
}
