package com.adproc8.booku.cart.controller;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientResponseException;

import com.adproc8.booku.cart.dto.BookIdsDto;
import com.adproc8.booku.cart.dto.GetCartResponseDto;
import com.adproc8.booku.cart.model.Cart;
import com.adproc8.booku.cart.model.PaymentDetails;
import com.adproc8.booku.cart.model.User;
import com.adproc8.booku.cart.service.CartService;

@RestController
@RequestMapping("/cart")
class CartController {

    private static final Logger logger = LoggerFactory.getLogger(CartController.class);

    private final CartService cartService;

    @Autowired
    CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("")
    ResponseEntity<GetCartResponseDto> getCart(
        @AuthenticationPrincipal User user,
        @RequestHeader("Authorization") String authHeader)
    {
        UUID userId = user.getId();

        Cart cart;
        try {
            cart = cartService.findByUserId(userId, authHeader);
        } catch (RestClientResponseException exception) {
            logger.error(exception.getMessage(), exception);
            return ResponseEntity.internalServerError().build();
        }

        GetCartResponseDto cartDto = GetCartResponseDto.builder()
                .cartId(cart.getId())
                .userId(userId)
                .paymentDetailsId(Optional.ofNullable(cart.getPaymentDetails())
                        .map(PaymentDetails::getId)
                        .orElse(null))
                .books(cart.getBooks())
                .build();

        return ResponseEntity.ok().body(cartDto);
    }

    @PatchMapping("/add-books")
    ResponseEntity<Void> addBookToCart(
        @AuthenticationPrincipal User user,
        @RequestHeader("Authorization") String authHeader,
        @RequestBody BookIdsDto dto)
    {
        Optional<Set<UUID>> dtoBookIds = Optional.ofNullable(dto.getBookIds());

        if (dtoBookIds.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        
        Set<UUID> bookIdsToAdd = dtoBookIds.get();

        UUID userId = user.getId();
        Cart cart;
        try {
            cart = cartService.findByUserId(userId, authHeader);
        } catch (RestClientResponseException exception) {
            logger.error(exception.getMessage(), exception);
            return ResponseEntity.internalServerError().build();
        }

        Set<UUID> currentBookIds = cart.getBookIds();
        currentBookIds.addAll(bookIdsToAdd);

        try {
            cartService.save(cart, authHeader);
        } catch (RestClientResponseException exception) {
            logger.error(exception.getMessage(), exception);
            return ResponseEntity.internalServerError().build();
        }

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/remove-books")
    ResponseEntity<Void> removeBookFromCart(
        @AuthenticationPrincipal User user,
        @RequestHeader("Authorization") String authHeader,
        @RequestBody BookIdsDto dto)
    {
        Optional<Set<UUID>> dtoBookIds = Optional.ofNullable(dto.getBookIds());

        if (dtoBookIds.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        
        Set<UUID> bookIdsToRemove = dtoBookIds.get();

        UUID userId = user.getId();
        Cart cart;
        try {
            cart = cartService.findByUserId(userId, authHeader);
        } catch (RestClientResponseException exception) {
            logger.error(exception.getMessage(), exception);
            return ResponseEntity.internalServerError().build();
        }

        Set<UUID> currentBookIds = cart.getBookIds();
        currentBookIds.removeAll(bookIdsToRemove);

        try {
            cartService.save(cart, authHeader);
        } catch (RestClientResponseException exception) {
            logger.error(exception.getMessage(), exception);
            return ResponseEntity.internalServerError().build();
        }

        return ResponseEntity.ok().build();
    }
}
