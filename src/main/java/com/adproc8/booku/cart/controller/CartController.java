package com.adproc8.booku.cart.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.adproc8.booku.cart.model.Cart;
import com.adproc8.booku.cart.model.User;
import com.adproc8.booku.cart.service.CartService;

@RestController
@RequestMapping("/cart")
class CartController {

    private final CartService cartService;

    @Autowired
    CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("")
    Cart getCart(@AuthenticationPrincipal User user) {
        UUID userId = user.getId();

        Cart cart = cartService.findByUserId(userId)
            .orElseGet(() -> {
                Cart newCart = Cart.builder()
                    .userId(userId)
                    .build();
                return cartService.save(newCart);
            });

        return cart;
    }
}
