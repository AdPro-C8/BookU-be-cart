package com.adproc8.booku.cart.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.adproc8.booku.cart.dto.CheckoutRequestDto;
import com.adproc8.booku.cart.model.Checkout;
import com.adproc8.booku.cart.model.User;
import com.adproc8.booku.cart.service.CheckoutService;

@RestController
@RequestMapping("/checkout")
class CheckoutController {

    private final CheckoutService checkoutService;

    @Autowired
    CheckoutController(CheckoutService checkoutService) {
        this.checkoutService = checkoutService;
    }

    @GetMapping("/{checkoutId}")
    ResponseEntity<Checkout> getCheckoutById(
        @PathVariable UUID checkoutId,
        @AuthenticationPrincipal User user)
    {
        Checkout checkout = checkoutService
            .findById(checkoutId)
            .orElseThrow();
        UUID checkoutOwnerId = checkout
            .getCart()
            .getUserId();

        UUID userId = user.getId();

        if (!userId.equals(checkoutOwnerId))
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(checkout);
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.OK)
    Checkout postCheckout(
        @RequestBody CheckoutRequestDto checkoutDto,
        @AuthenticationPrincipal User user)
    {
        String deliveryAddress = checkoutDto.getDeliveryAddress();

        Checkout newCheckout = Checkout.builder()
            .deliveryAddress(deliveryAddress)
            .build();

        checkoutService.save(newCheckout);

        return newCheckout;
    }
}
