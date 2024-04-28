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
import com.adproc8.booku.cart.model.User;
import com.adproc8.booku.cart.service.CartService;
import com.adproc8.booku.cart.service.UserService;

@RestController
@RequestMapping("/cart")
class CartController {

    private final CartService cartService;
    private final UserService userService;

    @Autowired
    CartController(CartService cartService, UserService userService) {
        this.cartService = cartService;
        this.userService = userService;
    }

    @GetMapping("")
    Cart getCart(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userService
            .findByUsername(userDetails.getUsername())
            .orElseThrow();
        UUID userId = user.getId();

        Cart cart = user.getCart();

        if (cart == null) {
            cart = Cart.builder()
                .id(new Cart.Id(UUID.randomUUID(), userId))
                .user(user)
                .books(List.of())
                .build();
            user.setCart(cart);
            userService.save(user);
        }

        return cart;
    }
}
