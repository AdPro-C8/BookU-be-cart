package com.adproc8.booku.cart.controller;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.adproc8.booku.cart.dto.BookIdsDto;
import com.adproc8.booku.cart.dto.GetCartResponseDto;
import com.adproc8.booku.cart.model.Cart;
import com.adproc8.booku.cart.model.Checkout;
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
    @ResponseStatus(HttpStatus.OK)
    GetCartResponseDto getCart(
        @AuthenticationPrincipal User user,
        @RequestHeader("Authorization") String authHeader)
    {
        UUID userId = user.getId();

        Cart cart = cartService.findByUserId(userId, authHeader)
            .orElseGet(() -> {
                Cart newCart = Cart.builder()
                    .userId(userId)
                    .build();
                return cartService.save(newCart);
            });

        GetCartResponseDto cartDto = GetCartResponseDto.builder()
                .cartId(cart.getId())
                .userId(userId)
                .checkoutId(Optional.ofNullable(cart.getCheckout())
                        .map(Checkout::getId)
                        .orElse(null))
                .books(cart.getBooks())
                .build();

        return cartDto;
    }

    @PatchMapping("/add-books")
    @ResponseStatus(HttpStatus.OK)
    void addBookToCart(
        @AuthenticationPrincipal User user,
        @RequestHeader("Authorization") String authHeader,
        @RequestBody BookIdsDto dto)
    {
        Set<UUID> bookIdsToAdd = dto.getBookIds(); 

        UUID userId = user.getId();

        Cart cart = cartService.findByUserId(userId, authHeader)
            .orElseGet(() -> {
                Cart newCart = Cart.builder()
                    .userId(userId)
                    .build();
                return cartService.save(newCart);
            });

        Set<UUID> currentBookIds = cart.getBookIds();
        currentBookIds.addAll(bookIdsToAdd);

        cartService.save(cart);
    }

    @PatchMapping("/remove-books")
    @ResponseStatus(HttpStatus.OK)
    void removeBookFromCart(
        @AuthenticationPrincipal User user,
        @RequestHeader("Authorization") String authHeader,
        @RequestBody BookIdsDto dto)
    {
        Set<UUID> bookIdsToRemove = dto.getBookIds(); 

        UUID userId = user.getId();

        Cart cart = cartService.findByUserId(userId, authHeader)
            .orElseGet(() -> {
                Cart newCart = Cart.builder()
                    .userId(userId)
                    .build();
                return cartService.save(newCart);
            });

        Set<UUID> currentBookIds = cart.getBookIds();
        currentBookIds.removeAll(bookIdsToRemove);

        cartService.save(cart);
    }
}
