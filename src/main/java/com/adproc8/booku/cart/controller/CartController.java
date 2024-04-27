package com.adproc8.booku.cart.controller;

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
import com.adproc8.booku.cart.service.CheckoutService;
import com.adproc8.booku.cart.service.UserService;

@RestController
@RequestMapping("/cart")
class CartController {

    private final CartService cartService;
    private final UserService userService;

    @Autowired
    CartController(CartService cartService, CheckoutService checkoutService, UserService userService) {
        this.cartService = cartService;
        this.userService = userService;

        User dummyUser = createDummyUser();
        userService.save(dummyUser);
    }

    private User createDummyUser() {
        User dummyUser = User.builder()
            .username("dummy")
            .password("dummy")
            .role("USER")
            .build();
        dummyUser.setCart(createDummyCart(dummyUser));
        return dummyUser;
    }

    private Cart createDummyCart(User user) {
        Cart.Id id = new Cart.Id(UUID.randomUUID(), user.getId());
        Cart cart = Cart.builder()
            .id(id)
            .user(user)
            .build();
        return cart;
    }

    @GetMapping("")
    Cart findCartById(@AuthenticationPrincipal UserDetails userDetails) {
        UUID userId = userService
            .findByUsername(userDetails.getUsername())
            .orElseThrow()
            .getId();

        Cart cart = cartService.findByUserId(userId)
            .orElse(Cart.builder()
                        .id(new Cart.Id(UUID.randomUUID(), userId))
                        .build());

        return cart;
    }
}
