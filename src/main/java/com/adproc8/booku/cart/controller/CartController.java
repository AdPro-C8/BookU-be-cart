package com.adproc8.booku.cart.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.adproc8.booku.cart.model.Cart;
import com.adproc8.booku.cart.service.CartService;

@RestController
@RequestMapping("/cart")
class CartController {

    private final CartService cartService;

    @Autowired
    CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    Cart getCart(@AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();

        Cart cart = cartService.findByUsername(username)
            .orElseGet(() -> {
                Cart newCart = Cart.builder()
                    .id(new Cart.Id(UUID.randomUUID(), username))
                    .books(List.of())
                    .build();
                return cartService.save(newCart);
            });

        return cart;
    }
}
